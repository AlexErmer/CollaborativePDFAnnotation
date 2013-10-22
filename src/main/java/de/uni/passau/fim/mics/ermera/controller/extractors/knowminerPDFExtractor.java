package de.uni.passau.fim.mics.ermera.controller.extractors;

import at.knowcenter.code.api.pdf.*;
import at.knowcenter.code.pdf.PdfExtractionPipeline;
import at.knowcenter.code.pdf.utils.PdfExtractionUtils;
import at.knowcenter.code.pdf.utils.rendering.PdfToImage;
import at.knowcenter.code.workers.configuration.configs.ExtractionConfiguration;
import de.uni.passau.fim.mics.ermera.model.BlockBean;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import de.uni.passau.fim.mics.ermera.model.PageBean;
import de.uni.passau.fim.mics.ermera.model.TooltipBean;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.SortedSet;

public class knowminerPDFExtractor implements Extractor {

    private final static int TOOLTIP_TEXT_LENGTH = 80;
    private final static PdfToImage.RendererType RENDERER_TYPE = PdfToImage.RendererType.Sun;
    private PdfExtractionPipeline pipeline;

    public knowminerPDFExtractor() {
        System.out.println("Loading models");
        ObjectMapper mapper = new ObjectMapper();
        try {
            ExtractionConfiguration config = mapper.readValue(knowminerPDFExtractor.class.getResourceAsStream("/extract-config-local.json"), ExtractionConfiguration.class);

            // load Pdf extractor pipeline
            pipeline = new PdfExtractionPipeline(config.blockModelFile, config.featuresFile,
                    config.tokenModelFile, config.languageModelDir);
        } catch (Throwable t) {
            throw new RuntimeException("Couldn't create server", t);
        }
        System.out.println("Loading models done");
    }

    public DocumentBean extract(String id) throws ExtractException {
        DocumentBean loadedDocumentBean = null;
        byte[] pdfByteArray;
        InputStream in = null;
        try {
            in = knowminerPDFExtractor.class.getResourceAsStream("/" + id + ".pdf");
            if (in == null) {
                throw new FileNotFoundException("/" + id + ".pdf");
            }
            pdfByteArray = IOUtils.toByteArray(in);
            loadedDocumentBean = processPdfData(id, pdfByteArray);
        } catch (FileNotFoundException e) {
            throw new ExtractException("File with id '" + id + "' not found.", e);
        } catch (PdfParser.PdfParserException e) {
            throw new ExtractException("Could not process file with id '" + id + "': " + e.getMessage(), e);
        } catch (IOException e) {
            throw new ExtractException("Could not read input stream: " + e.getMessage(), e);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                //noinspection ThrowFromFinallyBlock
                throw new ExtractException("Could not close the input stream: " + e.getMessage(), e);
            }
        }
        return loadedDocumentBean;
    }

    private synchronized DocumentBean processPdfData(String fileName, byte[] pdfByteArray) throws PdfParser.PdfParserException, IOException {
        InputStream in = new ByteArrayInputStream(pdfByteArray);
        Image[] images = new PdfToImage(RENDERER_TYPE).toImages(in);
        in.close();

        String[] imageFileNames = new String[images.length];
        for (int pageId = 0; pageId < images.length; pageId++) {
            File f = File.createTempFile(fileName + "_" + pageId, ".png");
            BufferedImage image = (BufferedImage) images[pageId];
            ImageIO.write(image, "png", f);
            imageFileNames[pageId] = f.getCanonicalPath();
        }

        in = new ByteArrayInputStream(pdfByteArray);
        File file = File.createTempFile("demoserver", ".pdf");
        FileOutputStream fos = new FileOutputStream(file);
        IOUtils.copy(in, fos);
        IOUtils.closeQuietly(fos);
        PdfExtractionPipeline.PdfExtractionResult result = pipeline.runPipeline(fileName, file);
        in.close();
        if (!file.delete()) {
            System.out.println("Could not delete temporary file: " + file.getName());
        }

        return createDocumentBean(fileName, imageFileNames, result);
    }

    private DocumentBean createDocumentBean(String id, String[] imageFileNames, PdfExtractionPipeline.PdfExtractionResult result) {
        DocumentBean documentBean = new DocumentBean();
        documentBean.setId(id);

        java.util.List<Page> pages = result.doc.getPages();
        for (Page page : pages) {
            int pageId = page.getNumber() - 1;

            PageBean pagebean = new PageBean();
            pagebean.setHeight((int) page.getHeight());
            pagebean.setWidth((int) page.getWidth());
            pagebean.setImagefilename(imageFileNames[pageId]);
            pagebean.setNumber(pageId + 1);

            int blockId = 0;
            SortedSet<Block> subBlocks = result.pageBlocks.get(pageId).getSubBlocks();
            for (Block block : subBlocks) {
                BlockBean blockBean = createBlockBean(block, pageId, blockId, "block", result);
                pagebean.addBlock(blockBean);
                blockId++;
            }

            documentBean.addPage(pagebean);
        }
        return documentBean;
    }

    private BlockBean createBlockBean(Block block, int pageId, int blockId, String cssClass, PdfExtractionPipeline.PdfExtractionResult result) {
        Document document = result.doc;
        BlockLabel label = result.labeling.getLabel(block);

        BlockBean blockBean = new BlockBean();
        BoundingBox bbox = block.getBoundingBox();
        blockBean.setHeight((int) (bbox.maxy - bbox.miny));
        blockBean.setWidth((int) (bbox.maxx - bbox.minx));
        blockBean.setLeft((int) bbox.minx);
        blockBean.setTop((int) bbox.miny);
        blockBean.setCssClass(label == null ? cssClass : cssClass + " " + cssClass + "-" + label.getLabel().toLowerCase());
        if (blockBean.getCssClass().contains("-title")
                || blockBean.getCssClass().contains("-subtitle")
                || blockBean.getCssClass().contains("-heading")
                || blockBean.getCssClass().contains("-main")) {
            blockBean.setSelectedBlock(true);
            if (!blockBean.getCssClass().contains("-main")) {
                blockBean.setHeadline(true);
            }
        }
        blockBean.setText(StringEscapeUtils.escapeHtml(pipeline.clearHyphenations(block)));
        blockBean.setId(String.format("%s_%d_%d", cssClass, pageId, blockId));
        blockBean.setOrder(result.postprocessedReadingOrder.getReadingOrder(pageId).indexOf(blockId) * 10);

        // tooltip for the box
        //TODO: refactor so tooltips are created on the fly (like lines)
        TooltipBean tooltipBean = new TooltipBean();
        tooltipBean.setLeft((int) (bbox.minx + (bbox.maxx - bbox.minx) + 10));
        tooltipBean.setTop((int) (bbox.miny));
        tooltipBean.setLabel(label == null ? "null" : label.getLabel());
        tooltipBean.setBbox(bbox.toString());
        String text = block.getText();
        text = (text.length() > TOOLTIP_TEXT_LENGTH) ? text.substring(0, TOOLTIP_TEXT_LENGTH) + "..." : text;
        tooltipBean.setText(StringEscapeUtils.escapeHtml(text));
        at.knowcenter.code.api.pdf.Font font = document.getFonts().get(PdfExtractionUtils.getMajorityFontId(block));
        tooltipBean.setFont(font.getFontName());
        tooltipBean.setSize(PdfExtractionUtils.getMajorityFontSize(block));
        if (font.getIsBold() != null) {
            tooltipBean.setBold(font.getIsBold().toString());
        }
        if (font.getIsItalic() != null) {
            tooltipBean.setItalic(font.getIsItalic().toString());
        }
        blockBean.setTooltipBean(tooltipBean);

        return blockBean;
    }
}
