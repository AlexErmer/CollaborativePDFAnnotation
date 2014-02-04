package de.uni.passau.fim.mics.ermera.opennlp;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import opennlp.tools.cmdline.StreamFactoryRegistry;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class OpenNLPServiceImpl implements OpenNLPService {

    //TODO: refactor for loose couling.. its now tied wirh brat
    private ObjectStream<NameSample> getStream(String userid) throws IOException {
        return StreamFactoryRegistry.getFactory(NameSample.class, "brat")
                .create(new String[]{
                        "-bratDataDir", PropertyReader.DATA_PATH + PropertyReader.BRATFOLDER + userid
                        , "-annotationConfig", PropertyReader.DATA_PATH + PropertyReader.BRATFOLDER + "annotation.conf"
                        //,"-recursive", "false"
                        //,"-sentenceDetectorModel", ""
                        //,"-tokenizerModel", ""
                        , "-ruleBasedTokenizer", "simple"
                });
    }

    /**
     * This method trains and returns a new {@code TokenNameFinderModel} for the entity given by {@code entityName}.
     *
     * @param userid     Id of the user which creates the model.
     * @param entityName the name of the entities for which the model is generated
     * @return the newly generated {@code TokenNameFinderModel}
     * @throws IOException exception for not being able to read files.
     */
    @Override
    public TokenNameFinderModel train(String userid, String entityName) throws IOException {
        ObjectStream<NameSample> sampleStream = getStream(userid);
        TokenNameFinderModel model;

        try {
            model = NameFinderME.train("en", entityName, sampleStream, Collections.<String, Object>emptyMap());
        } finally {
            sampleStream.close();
        }

        return model;
    }

    /**
     * This model uses a {@code TokenNameFinderModel} to find entities in the given documents.
     * The documents are encoded in a {@code Map<String, String>}. The key of the map is the document identifier and the value is the complete text of the document. <br />
     * It returns a {@code Map<String, NameFinderResult>}. The key is again the document identifier and the value is an object which contains the tokens and the found entities.
     *
     * @param model        The model which should be used.
     * @param documentStrs The Map of documents in which should be searched
     * @return a map which contains the found entities for each document.
     */
    @Override
    public Map<String, NameFinderResult> find(TokenNameFinderModel model, Map<String, String> documentStrs) throws NLPException {
        NameFinderME nameFinderME = new NameFinderME(model);

        // split strings into sentences and tokens
        InputStream sentIn = OpenNLPServiceImpl.class.getResourceAsStream("/en-sent.bin");
        InputStream tokIn = OpenNLPServiceImpl.class.getResourceAsStream("/en-token.bin");
        SentenceModel sentenceModel;
        TokenizerModel tokenizerModel;
        try {
            sentenceModel = new SentenceModel(sentIn);
            tokenizerModel = new TokenizerModel(tokIn);
        } catch (IOException e) {
            throw new NLPException("Could not load sentence- or tokenmodels", e);
        }
        SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModel);
        Tokenizer tokenizer = new TokenizerME(tokenizerModel);

        Map<String, NameFinderResult> results = new HashMap<>();
        List<String> tokens;
        for (Map.Entry<String, String> docStr : documentStrs.entrySet()) {
            tokens = new ArrayList<>();

            String[] sentences = sentenceDetector.sentDetect(docStr.getValue());
            for (String sentence : sentences) {
                tokens.addAll(Arrays.asList(tokenizer.tokenize(sentence)));
            }
            String[] tokensTmp = tokens.toArray(new String[tokens.size()]);

            Span[] nameSpans = nameFinderME.find(tokensTmp);
            results.put(docStr.getKey(), new NameFinderResult(tokensTmp, nameSpans));
        }
        return results;
    }
}
