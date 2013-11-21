package de.uni.passau.fim.mics.ermera.controller.actions.impl.docModActions;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class DocumentModificationAction implements Action {
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        MessageUtil mu = (MessageUtil) session.getAttribute(MessageUtil.NAME);

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
        doCustomAction(request, response, documentBean, pageNumber);

        Profile profile = (Profile) request.getSession().getAttribute("profile");
        String userid = profile.getMain().getProfileId();

        // finished everything.. store bean and also attach it to the request
        try {
            DocumentDao documentDao = new DocumentDaoImpl();
            documentDao.storeDocumentBean(userid, documentBean);
        } catch (IOException e) {
            mu.addMessage(MessageTypes.ERROR, "Could not save DocumentBean: " + e.getMessage());
        }

        return "display";
    }

    protected abstract void doCustomAction(HttpServletRequest request, HttpServletResponse response, DocumentBean documentBean, int pageNumber);
}
