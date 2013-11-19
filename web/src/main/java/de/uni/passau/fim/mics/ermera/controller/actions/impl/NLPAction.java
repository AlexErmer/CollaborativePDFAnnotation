package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import de.uni.passau.fim.mics.ermera.opennlp.OpenNLPServiceImpl;
import opennlp.tools.namefind.TokenNameFinderModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class NLPAction implements Action {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        DocumentBean documentBean = (DocumentBean) session.getAttribute("documentBean");
        if (documentBean == null) {
            request.setAttribute("errorMessage", "No loaded document");
            return "";
        }

        Profile profile = (Profile) request.getSession().getAttribute("profile");
        String userid = profile.getMain().getProfileId();

        //TODO: use model on new pdfs

        OpenNLPServiceImpl nlpService = new OpenNLPServiceImpl();
        TokenNameFinderModel model = nlpService.train(userid);

        // store model
        DocumentDao documentDao = new DocumentDaoImpl();
        documentDao.storeModel(userid, model);

        // redirect to homepage
        return "homepage";
    }
}