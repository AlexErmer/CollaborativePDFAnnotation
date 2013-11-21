package de.uni.passau.fim.mics.ermera.opennlp;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import opennlp.tools.cmdline.StreamFactoryRegistry;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.TokenNameFinderEvaluator;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.FMeasure;
import opennlp.tools.util.featuregen.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class OpenNLPServiceImpl {

    //TODO: refactor for loose couling.. its now tied wirh brat
    private ObjectStream<NameSample> getStream(String userid) throws IOException {
        ObjectStream<NameSample> nameSampleStream =
                StreamFactoryRegistry.getFactory(NameSample.class, "brat")
                        .create(new String[]{
                                "-bratDataDir", PropertyReader.DATA_PATH + PropertyReader.BRAT_WORKING_PATH + userid
                                , "-annotationConfig", PropertyReader.DATA_PATH + PropertyReader.BRAT_WORKING_PATH + "annotation.conf"
                                //,"-recursive", "false"
                                //,"-sentenceDetectorModel", ""
                                //,"-tokenizerModel", ""
                                , "-ruleBasedTokenizer", "simple"
                        });
        return nameSampleStream;
    }

    /**
     * This method trains and returns a new {@code TokenNameFinderModel} for the entity given by {@code entityName}.
     *
     * @param userid     Id of the user which creates the model.
     * @param entityName the name of the entities for which the model is generated
     * @return the newly generated {@code TokenNameFinderModel}
     * @throws IOException exception for not being able to read files.
     */
    public TokenNameFinderModel train(String userid, String entityName) throws IOException {
        ObjectStream<NameSample> sampleStream = getStream(userid);
        TokenNameFinderModel model;

        AdaptiveFeatureGenerator featureGenerator = new CachedFeatureGenerator(
                new AdaptiveFeatureGenerator[]{
                        new WindowFeatureGenerator(new TokenFeatureGenerator(), 2, 2),
                        new WindowFeatureGenerator(new TokenClassFeatureGenerator(true), 2, 2),
                        new OutcomePriorFeatureGenerator(),
                        new PreviousMapFeatureGenerator(),
                        new BigramNameFeatureGenerator(),
                        new SentenceFeatureGenerator(true, false)
                });
        try {
            model = NameFinderME.train("en", entityName, sampleStream, TrainingParameters.defaultParams(),
                    featureGenerator, Collections.<String, Object>emptyMap());

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
    public Map<String, NameFinderResult> find(TokenNameFinderModel model, Map<String, String> documentStrs) {
/*        //TODO: remove defaultmodel here!
        InputStream modelbb = OpenNLPServiceImpl.class.getResourceAsStream("/en-ner-person.bin");
        TokenNameFinderModel tnfm = null;
        try {
            tnfm = new TokenNameFinderModel(modelbb);
        } catch (IOException e) {
        }

        NameFinderME nameFinderME = new NameFinderME(tnfm);*/
        NameFinderME nameFinderME = new NameFinderME(model);

        // split strings into sentences and tokens
        InputStream sentIn = OpenNLPServiceImpl.class.getResourceAsStream("/en-sent.bin");
        InputStream tokIn = OpenNLPServiceImpl.class.getResourceAsStream("/en-token.bin");
        SentenceModel sentenceModel = null;
        TokenizerModel tokenizerModel = null;
        try {
            sentenceModel = new SentenceModel(sentIn);
            tokenizerModel = new TokenizerModel(tokIn);
        } catch (IOException e) {
        }
        SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModel);
        Tokenizer tokenizer = new TokenizerME(tokenizerModel);

        Map<String, NameFinderResult> results = new HashMap<>();
        List<String> tokens;
        for (Map.Entry<String, String> docStr : documentStrs.entrySet()) {
            tokens = new ArrayList<>();

            String sentences[] = sentenceDetector.sentDetect(docStr.getValue());
            for (String sentence : sentences) {
                tokens.addAll(Arrays.asList(tokenizer.tokenize(sentence)));
            }
            String[] tokensTmp = tokens.toArray(new String[tokens.size()]);

            Span nameSpans[] = nameFinderME.find(tokensTmp);
            results.put(docStr.getKey(), new NameFinderResult(tokensTmp, nameSpans));
        }
        return results;

    }

    private FMeasure evaluate(String userid, TokenNameFinderModel model) throws IOException {
        ObjectStream<NameSample> sampleStream = getStream(userid);

        TokenNameFinderEvaluator evaluator = new TokenNameFinderEvaluator(new NameFinderME(model));
        evaluator.evaluate(sampleStream);

        return evaluator.getFMeasure();
    }
}
