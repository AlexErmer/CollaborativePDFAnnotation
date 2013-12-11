package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;
import de.uni.passau.fim.mics.ermera.opennlp.OpenNLPServiceImpl;
import opennlp.tools.namefind.TokenNameFinderModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NLPAction implements Action {
    private MessageUtil mu;

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        mu = (MessageUtil) session.getAttribute(MessageUtil.NAME);

        Profile profile = (Profile) request.getSession().getAttribute("profile");
        String userid = profile.getMain().getProfileId();

        String[] files = request.getParameterValues("files");
        if (files == null) {
            mu.addMessage(MessageTypes.ERROR, "no files selected");
            return null;
        }

        if (request.getParameter("create") != null) {
            String modelname = request.getParameter("modelname");
            if (modelname == null) {
                mu.addMessage(MessageTypes.ERROR, "no modelname entered");
                return null;
            }

            createModel(userid, modelname, files);
        } else if (request.getParameter("use") != null) {
            String modelname = request.getParameter("modelselect");
            if (modelname == null) {
                mu.addMessage(MessageTypes.ERROR, "no modelname entered");
                return null;
            }

            Map<String, NameFinderResult> map = useModel(userid, modelname, files);
            request.getSession().setAttribute("resultMap", map);
            return "evaluation";
        }

        // redirect to homepage
        return "homepage";
    }

    private void createModel(String userid, String modelname, String[] files) throws IOException {
        //TODO: select documents which should form a new model
        OpenNLPServiceImpl nlpService = new OpenNLPServiceImpl();
        TokenNameFinderModel model = nlpService.train(userid, "Person");

        // store model
        DocumentDao documentDao = new DocumentDaoImpl();
        documentDao.storeModel(userid, modelname, model);
    }

    private Map<String, NameFinderResult> useModel(String userid, String modelname, String[] files) {
        OpenNLPServiceImpl nlpService = new OpenNLPServiceImpl();

        // model holen
        DocumentDao documentDao = new DocumentDaoImpl();
        TokenNameFinderModel model = null;
        try {
            model = documentDao.loadModel(userid, modelname);
        } catch (IOException e) {
            mu.addMessage(MessageTypes.ERROR, "Error loading the model: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            mu.addMessage(MessageTypes.ERROR, e.getMessage());
        }

        //get files as texts
        Map<String, String> documentStrs = new HashMap();
        for (String filename: files) {
            DocumentBean documentBean = null;
            try {
                documentBean = documentDao.loadDocumentBean(userid, filename);
            } catch (IOException e) {
                mu.addMessage(MessageTypes.ERROR, "Error loading the document " + filename + ": " + e.getMessage());
            } catch (ClassNotFoundException e) {
                mu.addMessage(MessageTypes.ERROR, e.getMessage());
            }

            documentStrs.put(documentBean.getId(), documentBean.toString());
        }

        // find entities
        return nlpService.find(model, documentStrs);
    }
}