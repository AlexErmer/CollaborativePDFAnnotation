package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.opennlp.overrides.MyBratNameSampleStream;
import de.uni.passau.fim.mics.ermera.opennlp.overrides.MyBratNameSampleStreamFactory;
import de.uni.passau.fim.mics.ermera.opennlp.MySpanAnnotation;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;
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

    private Map<String, BratDocument> bratDocumentMap = new HashMap<>();

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        //TODO: VIEL ZU LANGE MEHTODE
        //TODO: entitiytyp muss aus dem model kommen?!
        String type = "Person";

        // resultmap contains all finding from previous nlp action
        @SuppressWarnings("unchecked")
        Map<String, NameFinderResult> resultMap = (Map<String, NameFinderResult>) session.getAttribute("resultMap");

        //load bratannotations in a map
        try {
            createBratDocumentMap(userid);
        } catch (IOException e) {
            throw new ActionException("Fehler beim Erstellen der DocumentMap", e);
        }

        // documentloader for textsearch
        DocumentDao documentDao = new DocumentDaoImpl();

        // list of spans for later saving
        Map<String, List<MySpanAnnotation>> mySpanAnnotations = new HashMap<>();

        // set of filenames for later saving
        Set<String> filenames = new HashSet<>();


        // loop all selected findings
        String[] ids = request.getParameterValues("ok");
        if (ids != null ) {
            for (String id : ids) {
                // get NameFinderResult from id
                String[] idSplits = id.split("__");
                String filename = idSplits[1];
                String index = idSplits[2];
                Span span = resultMap.get(filename).getNameSpans()[Integer.valueOf(index)];

                // add this filename for later saving to the Set
                filenames.add(filename);

                // get offsets of this span
                int offsetStart = span.getStart();
                int offsetEnd = span.getEnd();

                // concat coveredtext
                String searchstr = resultMap.get(filename).getTokens()[offsetStart];
                for (int i = offsetStart + 1; i <= offsetEnd - 1; i++) {
                    searchstr = searchstr.concat(" " + resultMap.get(filename).getTokens()[i]);
                }

                // determine the real char offsets by searching for the text
                // TODO: implement caching to reduce IO actions
                String text;
                try {
                    text = documentDao.loadBratFile(userid, filename).replace(System.lineSeparator(),"~~");
                } catch (IOException e) {
                    throw new ActionException("Fehler beim Lesen des Bratfiles", e);
                }
                int hitStart = text.indexOf(searchstr);
                int hitEnd = hitStart + searchstr.length();

                // doublecheck found text matches
                if (searchstr.equals(text.substring(hitStart, hitEnd))) {
                    //scan if this annotation already exists
                    boolean found = false;

                    BratDocument bratdoc = bratDocumentMap.get(filename);
                    Collection<BratAnnotation> annos = bratdoc.getAnnotations();
                    for (BratAnnotation anno : annos) {
                        SpanAnnotation bspan = (SpanAnnotation) anno;
                        if (type.equals(bspan.getSpan().getType())
                                && hitStart == bspan.getSpan().getStart()
                                && hitEnd == bspan.getSpan().getEnd()) {
                            found = true;
                        }
                    }

                    int nextID = nextID(annos);

                    //if not, add this new one
                    if (!found) {
                        //Brat/Spanannoation kann ich nicht sebst erzeugen, weil sie protected im opennlp sind...
                        List<MySpanAnnotation> list = mySpanAnnotations.get(filename);
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(new MySpanAnnotation("T" + nextID++, type,
                                new Span(hitStart, hitEnd, type), searchstr));
                        mySpanAnnotations.put(filename, list);
                    }
                }
            }
        }
        if (mySpanAnnotations.isEmpty()) {
            mu.addMessage(MessageTypes.INFO, "Nothing to save selected");
        } else {
            // create new annotationfile with the new annotations recently accepted
            for (String filename : filenames) {
                try {
                    createNewAnnotationFile(userid, filename, mySpanAnnotations);
                } catch (IOException e) {
                    throw new ActionException("Fehler beim erstellen eines neuen Annotationsfiles", e);
                }
            }
            mu.addMessage(MessageTypes.SUCCESS, "Results saved");
        }
        return "homepage";
    }


    private void createBratDocumentMap(String userid) throws IOException {
        MyBratNameSampleStream stream = (MyBratNameSampleStream) getStream(userid);
        BratDocument bratdoc;
        while ((bratdoc = stream.getSamples().read()) != null) {
            bratDocumentMap.put(bratdoc.getId().substring(bratdoc.getId().lastIndexOf("\\")+1), bratdoc);
        }
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

    private void createNewAnnotationFile(String userid, String filename, Map<String, List<MySpanAnnotation>> mySpanAnnotations) throws IOException {
        StringBuilder sb = new StringBuilder();

        List<MySpanAnnotation> l = mySpanAnnotations.get(filename);
        for (MySpanAnnotation myAnno : l) {
            sb.append(myAnno.toString());
            sb.append(System.lineSeparator());
        }

        DocumentDao documentDao = new DocumentDaoImpl();
        documentDao.storeAnnotationFile(userid, filename, sb.toString());
    }

    private ObjectStream<NameSample> getStream(String userid) throws IOException {
        return new MyBratNameSampleStreamFactory()
                .create(new String[]{
                        "-bratDataDir", PropertyReader.DATA_PATH + PropertyReader.BRAT_WORKING_PATH + userid
                        , "-annotationConfig", PropertyReader.DATA_PATH + PropertyReader.BRAT_WORKING_PATH + "annotation.conf"
                        //,"-recursive", "false"
                        //,"-sentenceDetectorModel", ""
                        //,"-tokenizerModel", ""
                        , "-ruleBasedTokenizer", "simple"
                });
    }

}