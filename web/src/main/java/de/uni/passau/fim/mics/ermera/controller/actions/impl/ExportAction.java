package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
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
        MessageUtil mu = (MessageUtil) session.getAttribute(MessageUtil.NAME);

        DocumentDao documentDao = new DocumentDaoImpl();
        DocumentBean documentBean = null;

        Exporters exporterType = Exporters.valueOf(request.getParameter("type").toUpperCase());
        if (exporterType == null) {
            mu.addMessage(MessageTypes.ERROR, "Exportertype not found");
            return "";
        }

        String id = request.getParameter("id");
        if (id == null) {
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

        Exporter exporter = exporterType.getInstance();
        try {
            if (exporter.export(userid, documentBean)) {
                mu.addMessage(MessageTypes.SUCCESS,"export successful");
                return exporter.getRedirectURL(userid, documentBean.getId());
            }
        } catch (ExportException e) {
            mu.addMessage(MessageTypes.ERROR, e.getMessage());
        }

        // redirect to homepage in errorcase
        return "homepage";
    }
}