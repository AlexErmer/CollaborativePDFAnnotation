package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.controller.exporters.ExportException;
import de.uni.passau.fim.mics.ermera.controller.exporters.Exporter;
import de.uni.passau.fim.mics.ermera.controller.exporters.Exporters;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoException;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExportAction extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(ExportAction.class);

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        DocumentDao documentDao = new DocumentDaoImpl();
        DocumentBean documentBean;

        Exporters exporterType = Exporters.valueOf(request.getParameter("type").toUpperCase());
        if (exporterType == null) {
            mu.addMessage(MessageTypes.ERROR, "Exportertype not found");
            return null;
        }

        String id = request.getParameter("id");
        if (id == null) {
            mu.addMessage(MessageTypes.ERROR, "ID must not be null");
            return null;
        }

        Profile profile = (Profile) session.getAttribute("profile");
        String userid = profile.getMain().getProfileId();

        // get document model from dao
        try {
            documentBean = documentDao.loadDocumentBean(userid, id);
        } catch (DocumentDaoException e) {
            LOGGER.error("error while loading documentBean", e);
            mu.addMessage(MessageTypes.ERROR, "error while loading documentBean: " + e.getMessage());
            return "homepage";
        }

        Exporter exporter = exporterType.getInstance();
        try {
            if (exporter.export(userid, documentBean)) {
                return exporter.getRedirectURL(userid, documentBean.getId());
            }
        } catch (ExportException e) {
            LOGGER.error("ExportException", e);
            mu.addMessage(MessageTypes.ERROR, e.getMessage());
        }

        // redirect to homepage in errorcase
        return "homepage";
    }
}