package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.controller.exporters.ExportException;
import de.uni.passau.fim.mics.ermera.controller.exporters.Exporter;
import de.uni.passau.fim.mics.ermera.controller.exporters.Exporters;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExportAction implements Action {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        DocumentDao documentDao = new DocumentDaoImpl();
        DocumentBean documentBean = null;

        Exporters exporterType = Exporters.valueOf(request.getParameter("type").toUpperCase());
        if (exporterType == null) {
            request.setAttribute("errorMessage", "Exportertype not found");
            return "";
        }

        String id = request.getParameter("id");
        if (id == null) {
            request.setAttribute("errorMessage", "ID must not be null");
            return "";
        }

        Profile profile = (Profile) request.getSession().getAttribute("profile");
        String userid = profile.getMain().getProfileId();

        // get document model from dao
        try {
            documentBean = documentDao.loadDocumentBean(userid, id);
        } catch (FileNotFoundException e) {
            // do nothing if only the file was not found..
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Could not load saved file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            request.setAttribute("errorMessage", "Corrupt save? Could not find class: " + e.getMessage());
        }

        Exporter exporter = exporterType.getInstance();
        try {
            if (exporter.export(userid, documentBean)) {
                request.setAttribute("successMessage", "export successful");
                return exporter.getRedirectURL(userid, documentBean.getId());
            }
        } catch (ExportException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        // redirect to homepage in errorcase
        return "homepage";
    }
}