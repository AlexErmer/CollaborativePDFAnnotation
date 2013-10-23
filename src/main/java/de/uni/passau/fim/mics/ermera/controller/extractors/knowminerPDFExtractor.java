package de.uni.passau.fim.mics.ermera.controller.extractors;

import at.knowcenter.code.api.pdf.*;
import at.knowcenter.code.pdf.PdfExtractionPipeline;
import at.knowcenter.code.pdf.utils.rendering.PdfToImage;
import at.knowcenter.code.workers.configuration.configs.ExtractionConfiguration;
import de.uni.passau.fim.mics.ermera.model.BlockBean;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import de.uni.passau.fim.mics.ermera.model.PageBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.SortedSet;

public class knowminerPDFExtractor implements Extractor {

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

    public DocumentBean extract(String id, File file) throws ExtractException {
        try {
            InputStream in = new FileInputStream(file);
            Image[] images = new PdfToImage(RENDERER_TYPE).toImages(in);

            String[] imageFileNames = new String[images.length];
            for (int pageId = 0; pageId < images.length; pageId++) {
                File f = File.createTempFile(id + "_" + pageId, ".png");
                BufferedImage image = (BufferedImage) images[pageId];
                ImageIO.write(image, "png", f);
                imageFileNames[pageId] = f.getCanonicalPath();
            }

            PdfExtractionPipeline.PdfExtractionResult result = pipeline.runPipeline(id, file);
            in.close();

            return createDocumentBean(id, imageFileNames, result);
        } catch (PdfParser.PdfParserException e) {
            throw new ExtractException("Could not process file with id '" + id + "': " + e.getMessage(), e);
        } catch (IOException e) {
            throw new ExtractException("Could not read input stream: " + e.getMessage(), e);
        }
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

        return blockBean;
    }
}
