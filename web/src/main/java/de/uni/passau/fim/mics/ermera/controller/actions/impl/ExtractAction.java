package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.controller.extractors.ExtractException;
import de.uni.passau.fim.mics.ermera.controller.extractors.Extractor;
import de.uni.passau.fim.mics.ermera.controller.extractors.Extractors;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import de.uni.passau.fim.mics.ermera.model.IndexBean;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class ExtractAction extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(ExtractAction.class);

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        DocumentDao documentDao = new DocumentDaoImpl();
        DocumentBean documentBean = null;

        String type = request.getParameter("type");
        if (type == null) {
            type = (String) session.getAttribute("extract_type");
        }
        String id = request.getParameter("id");
        if (id == null) {
            id = (String) session.getAttribute("extract_id");
        }

        if (type == null || id == null) {
            return showExtractlist(request, documentDao);
        } else {
            Extractors extractorType = Extractors.valueOf(type.toUpperCase());

            // get document model from dao
            try {
                documentBean = documentDao.loadDocumentBean(userid, id);
            } catch (DocumentDaoException e) {
                //TODO dont show this message after uploading a new file.. there cant be documentbean...
                LOGGER.error("error while loading documentBean", e);
                mu.addMessage(MessageTypes.ERROR, "error while loading documentBean: " + e.getMessage());
            }

            // if no model found, extract it from real file
            if (documentBean == null) {
                File file = documentDao.loadPDF(userid, id);

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

    private String showExtractlist(HttpServletRequest request, DocumentDao documentDao) {
        IndexBean indexBean = new IndexBean();
        indexBean.setFileIds(documentDao.loadPDFFiles(userid));
        request.setAttribute("indexBean", indexBean);

        return "extract";
    }
}