package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.controller.exporters.ExportException;
import de.uni.passau.fim.mics.ermera.controller.exporters.Exporter;
import de.uni.passau.fim.mics.ermera.controller.exporters.Exporters;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ExportAction extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(ExportAction.class);

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        DocumentDao documentDao = new DocumentDaoImpl();
        DocumentBean documentBean;

        String all = request.getParameter("all");
        String id = request.getParameter("id");
        String type = request.getParameter("type").toUpperCase();

        Exporters exporterType;
        try {
            exporterType = Exporters.valueOf(type);
        } catch (IllegalArgumentException e) {
            mu.addMessage(MessageTypes.ERROR, "Exportertype \"" + type + "\" not found");
            LOGGER.error("Exportertype \"" + type + "\"  not found");
            return null;
        }
        Exporter exporter = exporterType.getInstance();

        if (all != null) {
            Map<String, Boolean> stringBooleanMap = documentDao.loadPDFFiles(userid);
            for (Map.Entry<String, Boolean> entry : stringBooleanMap.entrySet()) {
                if (entry.getValue()) {
                    try {
                        documentBean = documentDao.loadDocumentBean(userid, entry.getKey());
                        exporter.export(userid, documentBean);
                    } catch (DocumentDaoException e) {
                        LOGGER.error("error while loading documentBean", e);
                        mu.addMessage(MessageTypes.ERROR, "error while loading documentBean: " + e.getMessage());
                    } catch (ExportException e) {
                        LOGGER.error("ExportException", e);
                        mu.addMessage(MessageTypes.ERROR, e.getMessage());
                    }
                }
            }
            return null;
        } else if (id != null) {
            // get document model from dao
            try {
                documentBean = documentDao.loadDocumentBean(userid, id);
            } catch (DocumentDaoException e) {
                LOGGER.error("error while loading documentBean", e);
                mu.addMessage(MessageTypes.ERROR, "error while loading documentBean: " + e.getMessage());
                return null;
            }

            try {
                if (exporter.export(userid, documentBean)) {
                    return exporter.getRedirectURL(userid, documentBean.getId());
                }
            } catch (ExportException e) {
                LOGGER.error("ExportException", e);
                mu.addMessage(MessageTypes.ERROR, e.getMessage());
            }

            // redirect to homepage in errorcase
            return null;
        } else {
            mu.addMessage(MessageTypes.ERROR, "ID must not be null");
            return null;
        }

    }
}