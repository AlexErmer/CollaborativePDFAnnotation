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
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoException;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import de.uni.passau.fim.mics.ermera.model.IndexBean;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExtractAction implements Action {

    private static final Logger LOGGER = Logger.getLogger(ExtractAction.class);

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        MessageUtil mu = (MessageUtil) session.getAttribute(MessageUtil.NAME);
        Profile profile = (Profile) session.getAttribute("profile");
        String userid = profile.getMain().getProfileId();

        DocumentDao documentDao = new DocumentDaoImpl();
        DocumentBean documentBean = null;

        String type = request.getParameter("type");
        String id = request.getParameter("id");
        if (type == null || id == null) {
            ContentRepositoryDao contentRepositoryDao = new ContentRepositoryDaoImpl();
            IndexBean indexBean = new IndexBean();
            indexBean.setFileIds(contentRepositoryDao.getAllFileIDs(userid));
            request.setAttribute("indexBean", indexBean);
            return "extract";
        } else {
            Extractors extractorType = Extractors.valueOf(type.toUpperCase());

            // get document model from dao
            try {
                documentBean = documentDao.loadDocumentBean(userid, id);
            } catch (DocumentDaoException e) {
                LOGGER.error("error while loading documentBean", e);
                mu.addMessage(MessageTypes.ERROR, "error while loading documentBean: " + e.getMessage());
            }

            // if no model found, extract it from real file
            if (documentBean == null) {
                ContentRepositoryDao contentRepositoryDao = new ContentRepositoryDaoImpl();
                File file = contentRepositoryDao.load(userid, id);

                try {
                    Extractor extractor = extractorType.getInstance();
                    documentBean = extractor.extract(id, file);
                } catch (ExtractException e) {
                    LOGGER.error("ExtractException", e);
                    mu.addMessage(MessageTypes.ERROR, e.getMessage());
                    return "display";
                }
            }

            // finished everything.. store bean and also attach it to the request
            if (documentBean != null) {
                try {
                    documentDao.storeDocumentBean(userid, documentBean);
                } catch (IOException e) {
                    LOGGER.error("IO Could not save DocumentBean", e);
                    mu.addMessage(MessageTypes.ERROR, "Could not save DocumentBean: " + e.getMessage());
                }
                session.setAttribute("documentBean", documentBean);
            }

            return "display";
        }
    }
}