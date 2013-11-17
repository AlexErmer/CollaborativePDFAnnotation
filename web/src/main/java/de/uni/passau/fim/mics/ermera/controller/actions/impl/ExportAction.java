package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.controller.exporters.BratExporter;
import de.uni.passau.fim.mics.ermera.controller.exporters.ExportException;
import de.uni.passau.fim.mics.ermera.controller.exporters.Exporter;
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

        // export files to brat working directories
        Exporter exporter = new BratExporter();
        try {
            if (exporter.export(documentBean)) {
                request.setAttribute("successMessage", "Saved for brat");
            }
        } catch (ExportException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        // redirect to brat
        return "../brat/index.xhtml#/" + documentBean.getId();
    }
}