package de.uni.passau.fim.mics.ermera;

import at.knowcenter.code.api.pdf.*;
import at.knowcenter.code.pdf.PdfExtractionPipeline;
import at.knowcenter.code.pdf.blockclassification.BlockLabeling;
import at.knowcenter.code.pdf.utils.PdfExtractionUtils;
import at.knowcenter.code.pdf.utils.rendering.PdfToImage;
import at.knowcenter.code.workers.configuration.configs.ExtractionConfiguration;
import de.uni.passau.fim.mics.ermera.beans.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.SortedSet;

public class ExtractAction implements Action {

    private final static int TOOLTIP_TEXT_LENGTH = 80;
    private final static PdfToImage.RendererType RENDERER_TYPE = PdfToImage.RendererType.Sun;
    private PdfExtractionPipeline pipeline;

    public ExtractAction() {
        System.out.println("Loading models");
        ObjectMapper mapper = new ObjectMapper();
        try {
            ExtractionConfiguration config = mapper.readValue(ExtractAction.class.getResourceAsStream("/extract-config-local.json"), ExtractionConfiguration.class);

            // load Pdf extractor pipeline
            pipeline = new PdfExtractionPipeline(config.blockModelFile, config.featuresFile,
                    config.tokenModelFile, config.languageModelDir);
        } catch (Throwable t) {
            throw new RuntimeException("Couldn't create server", t);
        }
        System.out.println("Loading models done");
    }

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] pdfByteArray;
        String id = request.getParameter("id");
        InputStream in = null;
        try {
            in = ExtractAction.class.getResourceAsStream("/" + id + ".pdf");
            if (in == null) {
                throw new FileNotFoundException("/" + id + ".pdf");
            }
            pdfByteArray = IOUtils.toByteArray(in);
            DocumentBean documentBean = processPdfData(id, pdfByteArray);
            request.setAttribute("documentBean", documentBean);
        } catch (FileNotFoundException e) {
            request.setAttribute("errorMessage", "File with id '" + id + "' not found.");
        } catch (PdfParser.PdfParserException e) {
            request.setAttribute("errorMessage", "Could not process file with id '" + id + "': " + e.getMessage());
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Could not read input stream: " + e.getMessage());
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                request.setAttribute("errorMessage", "Could not close the input stream: " + e.getMessage());
            }
        }

        return "extract";
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
        return createDocumentBean(imageFileNames, result);
    }

    private DocumentBean createDocumentBean(String[] imageFileNames, PdfExtractionPipeline.PdfExtractionResult result) {
        List<Block> pageBlocks = result.pageBlocks;
        BlockLabeling labeling = result.labeling;
        Document document = result.doc;

        DocumentBean documentBean = new DocumentBean();
        for (int pageId = 0; pageId < pageBlocks.size(); pageId++) {
            PageBean pagebean = new PageBean();
            pagebean.setHeight((int) document.getPages().get(pageId).getHeight());
            pagebean.setWidth((int) document.getPages().get(pageId).getWidth());
            pagebean.setImagefilename(imageFileNames[pageId]);
            pagebean.setNumber(pageId + 1);

            createLineBean(result, pageId, pagebean);

            int blockId = 0;
            int tokenId = 0;
            SortedSet<Block> subBlocks = pageBlocks.get(pageId).getSubBlocks();
            for (Block block : subBlocks) {
                blockId++;

                BlockBean blockBean = createBlockBean(block, labeling.getLabel(block), pageId, blockId, "block", document);
                pagebean.appendBlock(blockBean);

                for (Block word : block.getWordBlocks()) {
                    if (!labeling.hasLabelOrNull(word, BlockLabel.Word)) {
                        tokenId++;
                        blockBean = createBlockBean(word, labeling.getLabel(word), pageId, tokenId, "token", document);
                        pagebean.appendBlock(blockBean);
                    }
                }
            }

            documentBean.appendPage(pagebean);
        }
        return documentBean;
    }

    private BlockBean createBlockBean(Block block, BlockLabel label, int pageId, int blockId, String cssClass, Document document) {
        BlockBean blockBean = new BlockBean();
        BoundingBox bbox = block.getBoundingBox();

        blockBean.setHeight((int) (bbox.maxy - bbox.miny));
        blockBean.setWidth((int) (bbox.maxx - bbox.minx));
        blockBean.setLeft((int) bbox.minx);
        blockBean.setTop((int) bbox.miny);
        blockBean.setCssClass(label == null ? cssClass : cssClass + " " + cssClass + "-" + label.getLabel().toLowerCase());
        blockBean.setText(StringEscapeUtils.escapeHtml(pipeline.clearHyphenations(block)));
        blockBean.setId(String.format("%s-%d-%d", cssClass, pageId, blockId));

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
        tooltipBean.setBold(font.getIsBold().toString());
        tooltipBean.setItalic(font.getIsItalic().toString());
        blockBean.setTooltipBean(tooltipBean);
        return blockBean;
    }

    private void createLineBean(PdfExtractionPipeline.PdfExtractionResult result, int pageId, PageBean pagebean) {
        List<Block> pageBlocks = result.pageBlocks;
        ReadingOrder readingOrder = result.postprocessedReadingOrder;
        List<Integer> order = readingOrder.getReadingOrder(pageId);
        Block[] blocksOnPage = pageBlocks.get(pageId).getSubBlocks().toArray(new Block[0]);
        for (int i = 0; i < order.size() - 1; i++) {
            int current = order.get(i);
            int next = order.get(i + 1);
            BoundingBox bbox1 = blocksOnPage[current].getBoundingBox();
            BoundingBox bbox2 = blocksOnPage[next].getBoundingBox();
            LineBean lineBean = new LineBean();
            lineBean.setX1((int) ((bbox1.maxx + bbox1.minx) / 2));
            lineBean.setY1((int) ((bbox1.maxy + bbox1.miny) / 2));
            lineBean.setX2((int) ((bbox2.maxx + bbox2.minx) / 2));
            lineBean.setY2((int) ((bbox2.maxy + bbox2.miny) / 2));
            pagebean.addLine(lineBean);
        }
    }
}
