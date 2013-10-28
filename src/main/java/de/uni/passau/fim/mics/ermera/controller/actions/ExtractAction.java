package de.uni.passau.fim.mics.ermera.controller.actions;

import de.uni.passau.fim.mics.ermera.controller.extractors.ExtractException;
import de.uni.passau.fim.mics.ermera.controller.extractors.Extractor;
import de.uni.passau.fim.mics.ermera.controller.extractors.KnowminerExtractor;
import de.uni.passau.fim.mics.ermera.dao.ContentRepositoryDao;
import de.uni.passau.fim.mics.ermera.dao.ContentRepositoryDaoImpl;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExtractAction implements Action {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        DocumentDao documentDao = new DocumentDaoImpl();
        DocumentBean documentBean = null;

        String id = request.getParameter("id");
        if (id == null){
            request.setAttribute("errorMessage", "ID must not be null");
            return "";
        }

        // get document model from dao
        try {
            documentBean = documentDao.load(id);
        } catch (FileNotFoundException e) {
            // do nothing if only the file was not found..
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Could not load saved file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            request.setAttribute("errorMessage", "Corrupt save? Could not find class: " + e.getMessage());
        }

        // if no model found, extract it from real file with knowminer
        if (documentBean == null) {
            ContentRepositoryDao contentRepositoryDao = new ContentRepositoryDaoImpl();
            File file = contentRepositoryDao.load(id);

            try {
                Extractor extractor = new KnowminerExtractor();
                documentBean = extractor.extract(id, file);
            } catch (ExtractException e) {
                request.setAttribute("errorMessage", e.getMessage());
                return "display";
            }
        }

        // finished everything.. store bean and also attach it to the request
        if (documentBean != null) {
            try {
                documentDao.store(documentBean);
            } catch (IOException e) {
                request.setAttribute("errorMessage", "Could not save DocumentBean: " + e.getMessage());
            }
            session.setAttribute("documentBean", documentBean);
        }

        return "display";
    }
}