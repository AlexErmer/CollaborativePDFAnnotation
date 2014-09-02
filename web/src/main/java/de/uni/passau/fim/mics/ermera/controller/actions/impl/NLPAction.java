package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.controller.ViewNames;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import de.uni.passau.fim.mics.ermera.opennlp.NLPException;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;
import de.uni.passau.fim.mics.ermera.opennlp.OpenNLPService;
import de.uni.passau.fim.mics.ermera.opennlp.OpenNLPServiceImpl;
import opennlp.tools.formats.brat.BratAnnotation;
import opennlp.tools.formats.brat.BratDocument;
import opennlp.tools.namefind.TokenNameFinderModel;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class NLPAction extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(NLPAction.class);

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        String[] files = request.getParameterValues("files");
        if (files == null) {
            mu.addMessage(MessageTypes.ERROR, "no files selected");
            return null;
        }

        if (request.getParameter("create") != null) {
            return handleCreateModel(request, userid, files);
        } else if (request.getParameter("use") != null) {
            return handleUseModel(request, session, userid, files);
        } else {
            return ViewNames.HOMEPAGE;
        }
    }

    private String handleUseModel(HttpServletRequest request, HttpSession session, String userid, String[] files) throws ActionException {
        String modelname = request.getParameter("modelselect");
        if (modelname == null) {
            mu.addMessage(MessageTypes.ERROR, "no modelname entered");
            return null;
        }

        List<NameFinderResult> list;
        try {
            list = useModel(userid, modelname, files);
        } catch (NLPException e) {
            throw new ActionException("Fehler beim Anwenden des NLP Modells", e);
        }
        list = filterExistingAnnotations(list);
        session.setAttribute("resultList", list);
        return ViewNames.EVALUATION;
    }

    // filter existing annotations from resultList
    private List<NameFinderResult> filterExistingAnnotations(List<NameFinderResult> resultList) {
        List<NameFinderResult> filteredResultListlteredResultList = new ArrayList<>();

        //TODO: refactor! cant use other action here!!
        EvaluationSaveAction saveAction = new EvaluationSaveAction();
        Map<String, BratDocument> bratDocumentMap = null;
        try {
            bratDocumentMap = saveAction.createBratDocumentMap(userid);
        } catch (ActionException e) {
            // nothing to do
            LOGGER.debug("ActionException occured", e);
        }

        for (NameFinderResult nfr : resultList) {
            BratDocument bratdoc = bratDocumentMap.get(nfr.getDocumentName());
            if (bratdoc != null) {

                Collection<BratAnnotation> annos = bratdoc.getAnnotations();
                List<NameFinderResult.Sentence> sentences = new ArrayList<>();
                for (NameFinderResult.Sentence sentence : nfr.getSentences()) {
                    List<NameFinderResult.Finding> findings = new ArrayList<>();
                    for (NameFinderResult.Finding finding : sentence.getFindingsList()) {
                        int start = sentence.getPosition().getStart() + sentence.getTokens().get(finding.getSpan().getStart()).getPosition().getStart();
                        int end = sentence.getPosition().getStart() + sentence.getTokens().get(finding.getSpan().getEnd() - 1).getPosition().getEnd();

                        boolean b = saveAction.checkAnnotationAlreadyExists(finding.getType(), start, end, annos);
                        if (!b) {
                            //filteredResultListlteredResultList.add(nfr);
                            findings.add(new NameFinderResult.Finding(finding));
                        }
                    }
                    if (!findings.isEmpty()) {
                        NameFinderResult.Sentence sentenceClone = new NameFinderResult.Sentence(sentence);
                        sentenceClone.setFindingsList(findings);
                        sentences.add(sentenceClone);
                    }
                }
                if (!sentences.isEmpty()) {
                    NameFinderResult clonedNFR = new NameFinderResult(nfr);
                    clonedNFR.setSentences(sentences);
                    filteredResultListlteredResultList.add(clonedNFR);
                }
            }
        }
        return filteredResultListlteredResultList;
    }

    private String handleCreateModel(HttpServletRequest request, String userid, String[] files) throws ActionException {
        String modelname = request.getParameter("modelname");
        if (modelname == null) {
            mu.addMessage(MessageTypes.ERROR, "no modelname entered");
            return null;
        }

        long zstVorher;
        long zstNachher;
        long diff;

        zstVorher = System.currentTimeMillis();

        createModel(userid, modelname, files);

        zstNachher = System.currentTimeMillis();
        diff = (zstNachher - zstVorher) / 1000;

        mu.addMessage(MessageTypes.SUCCESS, "model " + modelname + " created in " + diff + " seconds");
        return ViewNames.HOMEPAGE;
    }

    @SuppressWarnings({"UnusedParameters"})
    private void createModel(String userid, String modelname, String[] files) throws ActionException {
        try {
            //TODO: select documents which should form a new model
            OpenNLPService nlpService = new OpenNLPServiceImpl();
            TokenNameFinderModel model = nlpService.train(userid);

            // store model
            DocumentDao documentDao = new DocumentDaoImpl();
            documentDao.storeModel(userid, modelname, model);
        } catch (IOException e) {
            throw new ActionException("Fehler beim erzeugen des Models", e);
        }
    }

    private List<NameFinderResult> useModel(String userid, String modelname, String[] files) throws NLPException {
        OpenNLPService nlpService = new OpenNLPServiceImpl();

        // model holen
        DocumentDao documentDao = new DocumentDaoImpl();
        TokenNameFinderModel model = null;
        try {
            model = documentDao.loadModel(userid, modelname);
        } catch (IOException e) {
            LOGGER.error("IO error loading the model", e);
            mu.addMessage(MessageTypes.ERROR, "Error loading the model: " + e.getMessage());
        }
        if (model == null) {
            throw new NLPException("Unexpected! Model must not be null!");
        }

        //get files as texts
        Map<String, String> documentStrs = new HashMap<>();
        for (String filename : files) {
            DocumentBean documentBean;
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