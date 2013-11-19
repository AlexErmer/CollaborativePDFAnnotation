package de.uni.passau.fim.mics.ermera.opennlp;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import opennlp.tools.cmdline.StreamFactoryRegistry;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.TokenNameFinderEvaluator;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.FMeasure;
import opennlp.tools.util.featuregen.*;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

public class OpenNLPServiceImpl {

    public void executeNameFinder(String userid) throws IOException {
        TokenNameFinderModel model = train(userid);
        evaluate(userid, model);
    }

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

    private TokenNameFinderModel train(String userid) throws IOException {
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
            model = NameFinderME.train("en", "person", sampleStream, TrainingParameters.defaultParams(),
                    featureGenerator, Collections.<String, Object>emptyMap());

        } finally {
            sampleStream.close();
        }

        OutputStream modelOut = null;
        try {
            modelOut = new BufferedOutputStream(new FileOutputStream("model.txt"));
            model.serialize(modelOut);
        } finally {
            if (modelOut != null)
                modelOut.close();
        }

        return model;
    }

    private void evaluate(String userid, TokenNameFinderModel model) throws IOException {
        ObjectStream<NameSample> sampleStream = getStream(userid);

        TokenNameFinderEvaluator evaluator = new TokenNameFinderEvaluator(new NameFinderME(model));
        evaluator.evaluate(sampleStream);

        FMeasure result = evaluator.getFMeasure();

        System.out.println(result.toString());
    }
}
