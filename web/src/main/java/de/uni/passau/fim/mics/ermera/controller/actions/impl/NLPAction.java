package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoException;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import de.uni.passau.fim.mics.ermera.opennlp.NLPException;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;
import de.uni.passau.fim.mics.ermera.opennlp.OpenNLPServiceImpl;
import opennlp.tools.namefind.TokenNameFinderModel;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NLPAction implements Action {

    private static final Logger LOGGER = Logger.getLogger(NLPAction.class);

    private MessageUtil mu;

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        mu = (MessageUtil) session.getAttribute(MessageUtil.NAME);

        Profile profile = (Profile) session.getAttribute("profile");
        String userid = profile.getMain().getProfileId();

        String[] files = request.getParameterValues("files");
        if (files == null) {
            mu.addMessage(MessageTypes.ERROR, "no files selected");
            return null;
        }

        if (request.getParameter("create") != null) {
            String modelname = request.getParameter("modelname");
            String entitiytype = request.getParameter("entitiytype");
            if (modelname == null) {
                mu.addMessage(MessageTypes.ERROR, "no modelname entered");
                return null;
            }
            if (entitiytype == null) {
                mu.addMessage(MessageTypes.ERROR, "no entitiytype entered");
                return null;
            }

            createModel(userid, modelname, entitiytype, files);
        } else if (request.getParameter("use") != null) {
            String modelname = request.getParameter("modelselect");
            if (modelname == null) {
                mu.addMessage(MessageTypes.ERROR, "no modelname entered");
                return null;
            }

            Map<String, NameFinderResult> map = useModel(userid, modelname, files);
            session.setAttribute("resultMap", map);
            return "evaluation";
        }

        // redirect to homepage
        return "homepage";
    }

    private void createModel(String userid, String modelname, String entitiytype, String[] files) throws IOException {
        //TODO: select documents which should form a new model
        OpenNLPServiceImpl nlpService = new OpenNLPServiceImpl();
        TokenNameFinderModel model = nlpService.train(userid, entitiytype);

        // store model
        DocumentDao documentDao = new DocumentDaoImpl();
        documentDao.storeModel(userid, modelname, model);
    }

    private Map<String, NameFinderResult> useModel(String userid, String modelname, String[] files) throws NLPException {
        OpenNLPServiceImpl nlpService = new OpenNLPServiceImpl();

        // model holen
        DocumentDao documentDao = new DocumentDaoImpl();
        TokenNameFinderModel model = null;
        try {
            model = documentDao.loadModel(userid, modelname);
        } catch (IOException e) {
            LOGGER.error("IO error loading the model", e);
            mu.addMessage(MessageTypes.ERROR, "Error loading the model: " + e.getMessage());
        }

        //get files as texts
        Map<String, String> documentStrs = new HashMap();
        for (String filename : files) {
            DocumentBean documentBean = null;
            try {
                documentBean = documentDao.loadDocumentBean(userid, filename);
            } catch (DocumentDaoException e) {
                LOGGER.error("error while loading documentBean", e);
                mu.addMessage(MessageTypes.ERROR, "error while loading documentBean: " + e.getMessage());
                continue;
            }

            documentStrs.put(documentBean.getId(), documentBean.toString());
        }

        // find entities
        return nlpService.find(model, documentStrs);
    }
}