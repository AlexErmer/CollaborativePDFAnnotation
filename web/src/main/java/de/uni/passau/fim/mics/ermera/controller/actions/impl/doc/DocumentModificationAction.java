package de.uni.passau.fim.mics.ermera.controller.actions.impl.doc;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class DocumentModificationAction extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(DocumentModificationAction.class);

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        int pageNumber = 0;

        DocumentBean documentBean = (DocumentBean) session.getAttribute("documentBean");
        if (documentBean == null) {
            mu.addMessage(MessageTypes.ERROR, "No loaded document");
            return null;
        }

        // get pageNumber Paramater
        if (request.getParameter("pageNumber") != null) {
            pageNumber = Integer.valueOf(request.getParameter("pageNumber")) - 1;
        }

        // perform custom action
        doCustomAction(request, documentBean, pageNumber);

        // finished everything.. store bean and also attach it to the request
        try {
            DocumentDao documentDao = new DocumentDaoImpl();
            documentDao.storeDocumentBean(userid, documentBean);
        } catch (IOException e) {
            LOGGER.error("Could not save DocumentBean", e);
            mu.addMessage(MessageTypes.ERROR, "Could not save DocumentBean: " + e.getMessage());
        }

        return "display";
    }

    protected abstract void doCustomAction(HttpServletRequest request, DocumentBean documentBean, int pageNumber);
}
