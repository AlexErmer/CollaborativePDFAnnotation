package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.controller.Views;
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

public class ExtractAction extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(ExtractAction.class);

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        DocumentDao documentDao = new DocumentDaoImpl();
        DocumentBean documentBean = null;

        boolean isUploadMode = getParameter(request, "upload") == null;
        String type = getParameter(request, "type");
        String id = getParameter(request, "id");
        if (type == null || id == null) {
            return showExtractlist(request, documentDao);
        } else {
            Extractors extractorType = Extractors.valueOf(type.toUpperCase());

            // get document model from dao
            try {
                documentBean = documentDao.loadDocumentBean(userid, id);
            } catch (DocumentDaoException e) {
                if (isUploadMode) {
                    // dont show this message after uploading a new file.. there cant be documentbean...
                    LOGGER.error("error while loading documentBean", e);
                    mu.addMessage(MessageTypes.ERROR, "error while loading documentBean: " + e.getMessage());
                }
            }

            // if no model found, extract it from real file
            if (documentBean == null) {
                File file = documentDao.loadPDF(userid, id);

                try {
                    Extractor extractor = extractorType.getInstance();
                    documentBean = extractor.extract(id, file);
                    if (isUploadMode) {
                        // dont show this message after uploading a new file.. there cant be documentbean...
                        mu.addMessage(MessageTypes.SUCCESS, "everything ok, created a new one from scratch");
                    }
                } catch (ExtractException e) {
                    LOGGER.error("ExtractException", e);
                    mu.addMessage(MessageTypes.ERROR, e.getMessage());
                    return "extract";
                }

                // finished everything.. store bean
                try {
                    documentDao.storeDocumentBean(userid, documentBean);
                } catch (DocumentDaoException e) {
                    LOGGER.error("Error while saving documentBean", e);
                    mu.addMessage(MessageTypes.ERROR, "Error while saving documentBean: " + e.getMessage());
                }
            }

            session.setAttribute("documentBean", documentBean);
            return Views.DISPLAY.toString();
        }
    }

    private String getParameter(HttpServletRequest request, String parameterName) {
        String type = request.getParameter(parameterName);
        if (type == null) {
            type = (String) session.getAttribute("extract_" + parameterName);
            session.removeAttribute("extract_" + parameterName);
        }
        return type;
    }

    private String showExtractlist(HttpServletRequest request, DocumentDao documentDao) {
        IndexBean indexBean = new IndexBean();
        indexBean.setFileIds(documentDao.loadPDFFiles(userid));
        request.setAttribute("indexBean", indexBean);

        return "extract";
    }
}