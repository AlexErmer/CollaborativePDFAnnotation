package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.controller.ViewNames;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.EvaluationBean;
import de.uni.passau.fim.mics.ermera.opennlp.MySpanAnnotation;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderGroupedResultListItem;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;
import de.uni.passau.fim.mics.ermera.opennlp.SingleNameFinderResult;
import de.uni.passau.fim.mics.ermera.opennlp.overrides.MyBratNameSampleStream;
import de.uni.passau.fim.mics.ermera.opennlp.overrides.MyBratNameSampleStreamFactory;
import opennlp.tools.formats.brat.BratAnnotation;
import opennlp.tools.formats.brat.BratDocument;
import opennlp.tools.formats.brat.SpanAnnotation;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class EvaluationSaveAction extends AbstractAction {

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        // resultlist contains all finding from previous nlp action
        @SuppressWarnings("unchecked")
        List<NameFinderResult> resultList = (List<NameFinderResult>) session.getAttribute("resultList");

        // list of spans for later saving
        Map<String, List<MySpanAnnotation>> mySpanAnnotations = new HashMap<>();

        if ("true".equals(request.getParameter("grouped"))) {
            loopGroupedFindings(request, resultList, mySpanAnnotations);
        } else {
            loopFindings(request, resultList, mySpanAnnotations);
        }

        saveNewAnnotations(mySpanAnnotations);
        return ViewNames.HOMEPAGE;
    }

    private void loopGroupedFindings(HttpServletRequest request, List<NameFinderResult> resultList, Map<String, List<MySpanAnnotation>> mySpanAnnotations) throws ActionException {
        //load bratannotations in a map
        Map<String, BratDocument> bratDocumentMap = createBratDocumentMap(userid);

        EvaluationBean evaluationBean = (EvaluationBean) session.getAttribute("evaluationBean");

        // loop all SELECTED findings
        String[] ids = request.getParameterValues("ok");
        if (ids != null) {
            for (String id : ids) {
                // get NameFinderResult from id (format: nsc__findingIndex)
                String[] idSplits = id.split("__");
                int findingNumber = Integer.valueOf(idSplits[1]);
                NameFinderGroupedResultListItem nameFinderGroupedResultListItem = evaluationBean.getGroupedResultList().get(findingNumber);

                for (SingleNameFinderResult single : nameFinderGroupedResultListItem.getList()) {
                    NameFinderResult.Sentence sentence = single.getSentence();
                    NameFinderResult.Finding finding = single.getFinding();

                    int start = sentence.getPosition().getStart() + sentence.getTokens().get(finding.getSpan().getStart()).getPosition().getStart();
                    int end = sentence.getPosition().getStart() + sentence.getTokens().get(finding.getSpan().getEnd() - 1).getPosition().getEnd();

                    BratDocument bratdoc = bratDocumentMap.get(single.getDocumentName());
                    if (bratdoc != null) {
                        Collection<BratAnnotation> annos = bratdoc.getAnnotations();

                        //scan if this annotation already exists; if not, add this new one
                        if (!checkAnnotationAlreadyExists(finding.getType(), start, end, annos)) {
                            addNewAnnotation(mySpanAnnotations, finding.getType(), single.getDocumentName(), finding.getText(), start, end, annos);
                        }
                    }
                }
            }
        }
    }

    private void loopFindings(HttpServletRequest request, List<NameFinderResult> resultList, Map<String, List<MySpanAnnotation>> mySpanAnnotations) throws ActionException {
        //load bratannotations in a map
        Map<String, BratDocument> bratDocumentMap = createBratDocumentMap(userid);

        // loop all SELECTED findings
        String[] ids = request.getParameterValues("ok");
        if (ids != null) {
            for (String id : ids) {
                // get NameFinderResult from id (format: nsc__listIndex__sentenceIndex__findingIndex)
                String[] idSplits = id.split("__");
                int index = Integer.parseInt(idSplits[1]);
                String sentenceNumber = idSplits[2];
                String findingNumber = idSplits[3];

                NameFinderResult.Sentence sentence = resultList.get(index).getSentences().get(Integer.valueOf(sentenceNumber));
                NameFinderResult.Finding finding = sentence.getFindingsList().get(Integer.valueOf(findingNumber));

                int start = sentence.getPosition().getStart() + sentence.getTokens().get(finding.getSpan().getStart()).getPosition().getStart();
                int end = sentence.getPosition().getStart() + sentence.getTokens().get(finding.getSpan().getEnd() - 1).getPosition().getEnd();

                BratDocument bratdoc = bratDocumentMap.get(resultList.get(index).getDocumentName());
                if (bratdoc != null) {
                    Collection<BratAnnotation> annos = bratdoc.getAnnotations();

                    //scan if this annotation already exists; if not, add this new one
                    if (!checkAnnotationAlreadyExists(finding.getType(), start, end, annos)) {
                        addNewAnnotation(mySpanAnnotations, finding.getType(), resultList.get(index).getDocumentName(), finding.getText(), start, end, annos);
                    }
                }
            }
        }
    }

    private void addNewAnnotation(Map<String, List<MySpanAnnotation>> mySpanAnnotations, String type, String filename, String searchstr, int hitStart, int hitEnd, Collection<BratAnnotation> annos) {
        int nextID = nextID(annos) + (mySpanAnnotations.get(filename) != null ? mySpanAnnotations.get(filename).size() : 0);
        //Brat/Spanannoation kann ich nicht sebst erzeugen, weil sie protected im opennlp sind...
        List<MySpanAnnotation> list = mySpanAnnotations.get(filename);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(new MySpanAnnotation("T" + nextID, type,
                new Span(hitStart, hitEnd, type), searchstr));
        mySpanAnnotations.put(filename, list);
    }

    public boolean checkAnnotationAlreadyExists(String type, int hitStart, int hitEnd, Collection<BratAnnotation> annos) {
        //IDEA: kann man hier nicht Collection.contains()  verwenden?!
        for (BratAnnotation anno : annos) {
            SpanAnnotation bspan = (SpanAnnotation) anno;
            if (type.equals(bspan.getSpan().getType())
                    && hitStart == bspan.getSpan().getStart()
                    && hitEnd == bspan.getSpan().getEnd()) {
                return true;
            }
        }
        return false;
    }

    public Map<String, BratDocument> createBratDocumentMap(String userid) throws ActionException {
        Map<String, BratDocument> bratDocumentMap = new HashMap<>();
        try {
            MyBratNameSampleStream stream = (MyBratNameSampleStream) getStream(userid);
            BratDocument bratdoc;
            while ((bratdoc = stream.getSamples().read()) != null) {
                bratDocumentMap.put(bratdoc.getId().substring(bratdoc.getId().lastIndexOf("\\") + 1), bratdoc);
            }
        } catch (IOException e) {
            throw new ActionException("Fehler beim Erstellen der DocumentMap", e);
        }
        return bratDocumentMap;
    }

    /**
     * Gets the next annotation id from a brat annotated file.
     *
     * @param annos collection of all existing annotations
     * @return id of the next annotation as a string
     */
    private int nextID(Collection<BratAnnotation> annos) {
        int highestnumber = 0;
        for (BratAnnotation anno : annos) {
            if ("T".equals(anno.getId().substring(0, 1))) {
                int number = Integer.parseInt(anno.getId().substring(1));
                if (number > highestnumber) {
                    highestnumber = number;
                }
            }
        }
        return highestnumber + 1;
    }

    private void saveNewAnnotations(Map<String, List<MySpanAnnotation>> mySpanAnnotations) throws ActionException {
        if (mySpanAnnotations.isEmpty()) {
            mu.addMessage(MessageTypes.INFO, "Nothing to save selected");
        } else {
            // create new annotationfile with the new annotations recently accepted
            for (Map.Entry<String, List<MySpanAnnotation>> entry : mySpanAnnotations.entrySet()) {
                try {
                    createNewAnnotationFile(userid, entry.getKey(), entry.getValue());
                } catch (IOException e) {
                    throw new ActionException("Fehler beim Erstellen eines neuen Annotationsfiles", e);
                }
            }
            mu.addMessage(MessageTypes.SUCCESS, "Results saved");
        }
    }

    private void createNewAnnotationFile(String userid, String filename, List<MySpanAnnotation> mySpanAnnotationList) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (MySpanAnnotation myAnno : mySpanAnnotationList) {
            sb.append(myAnno.toString());
            sb.append(System.lineSeparator());
        }

        DocumentDao documentDao = new DocumentDaoImpl();
        documentDao.storeAnnotationFile(userid, filename, sb.toString());
    }

    private ObjectStream<NameSample> getStream(String userid) throws IOException {
        return new MyBratNameSampleStreamFactory()
                .create(new String[]{
                        "-bratDataDir", PropertyReader.DATA_PATH + PropertyReader.BRATFOLDER + userid
                        , "-annotationConfig", PropertyReader.DATA_PATH + PropertyReader.BRATFOLDER + userid + "\\annotation.conf"
                        //,"-recursive", "false"
                        //,"-sentenceDetectorModel", ""
                        //,"-tokenizerModel", ""
                        , "-ruleBasedTokenizer", "simple"
                });
    }

}