package de.uni.passau.fim.mics.ermera.actions;

import de.uni.passau.fim.mics.ermera.BratConnector;
import de.uni.passau.fim.mics.ermera.Storage;
import de.uni.passau.fim.mics.ermera.beans.DocumentBean;
import de.uni.passau.fim.mics.ermera.exceptions.ExtractException;
import de.uni.passau.fim.mics.ermera.extractors.PDFExtractor;
import de.uni.passau.fim.mics.ermera.extractors.knowminerPDFExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExtractAction implements Action {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        int pageNumber = 0;
        Storage storage = new Storage();
        DocumentBean loadedDocumentBean = null;

        // get documentBean from storage, if none found extract it from file
        try {
            loadedDocumentBean = storage.load(id);
        } catch (FileNotFoundException e) {
            // do nothing if only the file was not found..
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Could not load saved file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            request.setAttribute("errorMessage", "Corrupt save? Could not find class: " + e.getMessage());
        }

        if (loadedDocumentBean == null) {
            try {
                PDFExtractor pdfExtractor = new knowminerPDFExtractor();
                loadedDocumentBean = pdfExtractor.extract(id);
            } catch (ExtractException e) {
                request.setAttribute("errorMessage", e.getMessage());
                return "extract";
            }
        }

        // get pageNumber Paramater
        if (request.getParameter("pageNumber") != null) {
            pageNumber = Integer.valueOf(request.getParameter("pageNumber")) - 1;
        }

        // react on action?
        String action = request.getParameter("action");
        if (action != null) {
            System.out.println("handling action: " + action);
            switch (action) {
                case "sort":
                    String[] items = request.getParameterValues("items[]");
                    if (items != null) {
                        loadedDocumentBean.reorderPageBean(pageNumber, items);
                    }
                    break;
                case "unselect":
                    loadedDocumentBean.unselectBlock(pageNumber, request.getParameter("item"));
                    break;
                case "toggleHeadline":
                    //TODO implement
                    break;
                case "saveForBrat":
                    try {
                        if (BratConnector.saveForBrat(loadedDocumentBean)) {
                            request.setAttribute("successMessage", "Saved for brat");
                        }
                    } catch (IOException e) {
                        request.setAttribute("errorMessage", "Could not save Text for Brat: " + e.getMessage());
                    }
                    break;
                default:
                    break;
            }
        }

        // finished everything.. store bean and also attach it to the request
        if (loadedDocumentBean != null) {
            try {
                storage.store(loadedDocumentBean);
            } catch (IOException e) {
                request.setAttribute("errorMessage", "Could not save DocumentBean: " + e.getMessage());
            }
            request.setAttribute("documentBean", loadedDocumentBean);
        }

        return "extract";
    }
}