package de.uni.passau.fim.mics.ermera;

import at.knowcenter.code.api.pdf.*;
import at.knowcenter.code.pdf.PdfExtractionPipeline;
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
        String id = request.getParameter("id");

        // get documentBean from storage
        DocumentBean loadedDocumentBean = loadPDFFromStorage(request, id);

        // none in storage? then extract it from request
        if (loadedDocumentBean == null) {
            loadedDocumentBean = extract(request, id);
        }

        // react on action?
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "sort":
                    String[] items = request.getParameterValues("items[]");
                    if (items != null) {
                        loadedDocumentBean.sortBlocks(items);
                    }
                    break;
                case "drop":
                    break;
                default:
                    break;
            }
        }

        // finished everything.. store bean and also attach it to the request
        if (loadedDocumentBean != null) {
            storePDF(request, id, loadedDocumentBean);
            request.setAttribute("documentBean", loadedDocumentBean);
        }

        return "extract";
    }

    //TODO handle storage
    private DocumentBean loadPDFFromStorage(HttpServletRequest request, String id) {
        return (DocumentBean) request.getSession().getAttribute("pdf_" + id);
    }

    private void storePDF(HttpServletRequest request, String id, DocumentBean documentBean) {
        request.getSession().setAttribute("pdf_" + id, documentBean);
    }

    private DocumentBean extract(HttpServletRequest request, String id) {
        DocumentBean loadedDocumentBean = null;
        byte[] pdfByteArray;
        InputStream in = null;
        try {
            in = ExtractAction.class.getResourceAsStream("/" + id + ".pdf");
            if (in == null) {
                throw new FileNotFoundException("/" + id + ".pdf");
            }
            pdfByteArray = IOUtils.toByteArray(in);
            loadedDocumentBean = processPdfData(id, pdfByteArray);
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
        return createDocumentBean(imageFileNames, result);
    }

    private DocumentBean createDocumentBean(String[] imageFileNames, PdfExtractionPipeline.PdfExtractionResult result) {
        DocumentBean documentBean = new DocumentBean();

        List<Page> pages = result.doc.getPages();
        for (Page page : pages) {
            int pageId = page.getNumber() - 1;

            PageBean pagebean = new PageBean();
            pagebean.setHeight((int) page.getHeight());
            pagebean.setWidth((int) page.getWidth());
            pagebean.setImagefilename(imageFileNames[pageId]);
            pagebean.setNumber(pageId + 1);

            createLineBean(result, pageId, pagebean);

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
        }
        blockBean.setText(StringEscapeUtils.escapeHtml(pipeline.clearHyphenations(block)));
        blockBean.setId(String.format("%s_%d_%d", cssClass, pageId, blockId));
        blockBean.setOrder(result.postprocessedReadingOrder.getReadingOrder(pageId).indexOf(blockId) * 10);

        // tooltip for the box
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

    //TODO: move to pagebean to generate lines from (custom) selected texts
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
