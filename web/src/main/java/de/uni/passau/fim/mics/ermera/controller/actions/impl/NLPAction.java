package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import de.uni.passau.fim.mics.ermera.opennlp.OpenNLPServiceImpl;
import opennlp.tools.namefind.TokenNameFinderModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class NLPAction implements Action {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        MessageUtil mu = (MessageUtil) session.getAttribute(MessageUtil.NAME);

        Profile profile = (Profile) request.getSession().getAttribute("profile");
        String userid = profile.getMain().getProfileId();

        String[] files = request.getParameterValues("files");

        if (request.getParameter("create") != null) {
            String modelname = request.getParameter("modelname");
            if (modelname == null) {
                mu.addMessage(MessageTypes.ERROR, "no modelname entered");
                return "";
            }
            createModel(userid, modelname, files);
        } else if (request.getParameter("use") != null) {
            useModel(userid, files);
        }

        // redirect to homepage
        return "homepage";
    }

    private void createModel(String userid, String modelname, String[] files) throws IOException {
        //TODO: select documents which should form a new model
        //TODO: save model with given name
        OpenNLPServiceImpl nlpService = new OpenNLPServiceImpl();
        TokenNameFinderModel model = nlpService.train(userid);

        // store model
        DocumentDao documentDao = new DocumentDaoImpl();
        documentDao.storeModel(userid, modelname, model);
    }

    private void useModel(String userid, String[] files) {
        //TODO: implement
    }
}