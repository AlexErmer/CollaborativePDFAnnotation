package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.controller.extractors.ExtractException;
import de.uni.passau.fim.mics.ermera.controller.extractors.Extractor;
import de.uni.passau.fim.mics.ermera.controller.extractors.Extractors;
import de.uni.passau.fim.mics.ermera.dao.content.ContentRepositoryDao;
import de.uni.passau.fim.mics.ermera.dao.content.ContentRepositoryDaoImpl;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
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
        MessageUtil mu = (MessageUtil) session.getAttribute(MessageUtil.NAME);

        DocumentDao documentDao = new DocumentDaoImpl();
        DocumentBean documentBean = null;

        Extractors extractorType = Extractors.valueOf(request.getParameter("type").toUpperCase());
        if (extractorType == null) {
            mu.addMessage(MessageTypes.ERROR, "Extractorstype not found");
            return "";
        }

        String id = request.getParameter("id");
        if (id == null){
            mu.addMessage(MessageTypes.ERROR, "ID must not be null");
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
            mu.addMessage(MessageTypes.ERROR, "Could not load saved file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            mu.addMessage(MessageTypes.ERROR, "Corrupt save? Could not find class: " + e.getMessage());
        }

        // if no model found, extract it from real file
        if (documentBean == null) {
            ContentRepositoryDao contentRepositoryDao = new ContentRepositoryDaoImpl();
            File file = contentRepositoryDao.load(userid, id);

            try {
                Extractor extractor = extractorType.getInstance();
                documentBean = extractor.extract(id, file);
            } catch (ExtractException e) {
                mu.addMessage(MessageTypes.ERROR, e.getMessage());
                return "display";
            }
        }

        // finished everything.. store bean and also attach it to the request
        if (documentBean != null) {
            try {
                documentDao.storeDocumentBean(userid, documentBean);
            } catch (IOException e) {
                mu.addMessage(MessageTypes.ERROR, "Could not save DocumentBean: " + e.getMessage());
            }
            session.setAttribute("documentBean", documentBean);
        }

        return "display";
    }
}