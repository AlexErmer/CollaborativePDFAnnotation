package de.uni.passau.fim.mics.ermera.controller.actions.impl.docModActions;

import com.mendeley.oapi.schema.Profile;
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
        int pageNumber = 0;

        HttpSession session = request.getSession();
        DocumentBean documentBean = (DocumentBean) session.getAttribute("documentBean");
        if (documentBean == null) {
            request.setAttribute("errorMessage", "No loaded document");
            return "";
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
            documentDao.store(userid, documentBean);
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Could not save DocumentBean: " + e.getMessage());
        }

        return "display";
    }

    protected abstract void doCustomAction(HttpServletRequest request, HttpServletResponse response, DocumentBean documentBean, int pageNumber);
}
