package de.uni.passau.fim.mics.ermera.controller.actions;

import de.uni.passau.fim.mics.ermera.controller.exporters.BratConnector;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExportAction implements Action {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        DocumentDao documentDao = new DocumentDaoImpl();
        DocumentBean loadedDocumentBean = null;

        // get documentBean from documentDaoImpl, if none found extract it from file
        try {
            loadedDocumentBean = documentDao.load(id);
        } catch (FileNotFoundException e) {
            // do nothing if only the file was not found..
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Could not load saved file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            request.setAttribute("errorMessage", "Corrupt save? Could not find class: " + e.getMessage());
        }

        try {
            if (BratConnector.saveForBrat(loadedDocumentBean)) {
                request.setAttribute("successMessage", "Saved for brat");
            }
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Could not save Text for Brat: " + e.getMessage());
        }

        // redirect to brat
        return "../brat/index.xhtml#/" + id;
    }
}