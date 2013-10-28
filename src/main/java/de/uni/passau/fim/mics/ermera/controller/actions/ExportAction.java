package de.uni.passau.fim.mics.ermera.controller.actions;

import de.uni.passau.fim.mics.ermera.controller.exporters.BratConnector;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ExportAction implements Action {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        DocumentBean documentBean = (DocumentBean) session.getAttribute("documentBean");
        if (documentBean == null) {
            request.setAttribute("errorMessage", "No loaded document");
            return "";
        }

        try {
            if (BratConnector.saveForBrat(documentBean)) {
                request.setAttribute("successMessage", "Saved for brat");
            }
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Could not save Text for Brat: " + e.getMessage());
        }

        // redirect to brat
        return "../brat/index.xhtml#/" + documentBean.getId();
    }
}