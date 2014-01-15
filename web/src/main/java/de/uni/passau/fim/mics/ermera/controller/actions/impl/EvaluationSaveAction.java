package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import de.uni.passau.fim.mics.ermera.opennlp.MyBratNameSampleStream;
import de.uni.passau.fim.mics.ermera.opennlp.MyBratNameSampleStreamFactory;
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

public class EvaluationSaveAction implements Action {

    private Map<String, BratDocument> bratDocumentMap = new HashMap<>();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //TODO: VIEL ZU LANGE MEHTODE
        MessageUtil mu = (MessageUtil) request.getSession().getAttribute(MessageUtil.NAME);
        Profile profile = (Profile) request.getSession().getAttribute("profile");
        String userid = profile.getMain().getProfileId();

        //TODO: entitiytyp muss aus dem model kommen?!
        String type = "Person";

        // resultmap contains all finding from previous nlp action
        @SuppressWarnings("unchecked")
        Map<String, NameFinderResult> resultMap = (Map<String, NameFinderResult>) request.getSession().getAttribute("resultMap");

        //load bratannotations in a map
        createBratDocumentMap(userid);

        // documentloader for textsearch
        DocumentDao documentDao = new DocumentDaoImpl();

        // list of spans for later saving
        Map<String, List<MySpanAnnotation>> mySpanAnnotations = new HashMap<>();

        // set of filenames for later saving
        Set<String> filenames = new HashSet<>();


        // loop all selected findings
        String[] ids = request.getParameterValues("ok");
        if (ids != null ) for (String id : ids) {
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
            String text = documentDao.loadBratFile(userid, filename).replace(System.lineSeparator(),"~~");
            int hit_start = text.indexOf(searchstr);
            int hit_end = hit_start + searchstr.length();

            // doublecheck found text matches
            if (searchstr.equals(text.substring(hit_start, hit_end))) {
                //scan if this annotation already exists
                boolean found = false;

                BratDocument bratdoc = bratDocumentMap.get(filename);
                Collection<BratAnnotation> annos = bratdoc.getAnnotations();
                for (BratAnnotation anno : annos) {
                    SpanAnnotation bspan = (SpanAnnotation) anno;
                    if (type.equals(bspan.getSpan().getType())
                            && hit_start == bspan.getSpan().getStart()
                            && hit_end == bspan.getSpan().getEnd()) {
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
                            new Span(hit_start, hit_end, type), searchstr));
                    mySpanAnnotations.put(filename, list);
                }
            }
        }
        if (mySpanAnnotations.size() == 0) {
            mu.addMessage(MessageTypes.INFO, "Nothing to save selected");
        } else {
            // create new annotationfile with the new annotations recently accepted
            for (String filename : filenames) {
                createNewAnnotationFile(userid, filename, mySpanAnnotations);
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
            if (anno.getId().substring(0, 1).equals("T")) {
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