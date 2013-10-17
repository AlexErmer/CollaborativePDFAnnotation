package de.uni.passau.fim.mics.ermera.actions;

import de.uni.passau.fim.mics.ermera.Storage;
import de.uni.passau.fim.mics.ermera.beans.DocumentBean;
import de.uni.passau.fim.mics.ermera.exceptions.ExtractException;
import de.uni.passau.fim.mics.ermera.extractors.PDFExtractor;
import de.uni.passau.fim.mics.ermera.extractors.knowminerPDFExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExtractAction implements Action {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        Storage storage = new Storage(request);

        // get documentBean from storage, if none found extract it from file
        DocumentBean loadedDocumentBean = storage.load(id);
        if (loadedDocumentBean == null) {
            try {
                PDFExtractor pdfExtractor = new knowminerPDFExtractor();
                loadedDocumentBean = pdfExtractor.extract(id);
            } catch (ExtractException e) {
                request.setAttribute("errorMessage", e.getMessage());
                return "extract";
            }
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
                case "remove":
                    loadedDocumentBean.removeBlock(request.getParameter("item"));
                    break;
                default:
                    break;
            }
        }

        // finished everything.. store bean and also attach it to the request
        if (loadedDocumentBean != null) {
            storage.store(id, loadedDocumentBean);
            request.setAttribute("documentBean", loadedDocumentBean);
        }

        return "extract";
    }
}