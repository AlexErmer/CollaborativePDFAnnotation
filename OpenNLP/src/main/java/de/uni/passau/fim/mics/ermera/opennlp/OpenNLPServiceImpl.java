package de.uni.passau.fim.mics.ermera.opennlp;

import opennlp.tools.namefind.*;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.FMeasure;
import opennlp.tools.util.featuregen.*;
import opennlp.tools.util.model.ModelType;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;

public class OpenNLPServiceImpl {

    public void executeNameFinder() throws IOException {
        TokenNameFinderModel model = train();
        evaluate(model);
    }

    private ObjectStream<NameSample> getStream() throws IOException {
        Charset charset = Charset.forName("UTF-8");
        InputStream inputStream = getClass().getResourceAsStream("/en-ner-person.train");
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStream, charset);
        return new NameSampleDataStream(lineStream);
    }

    private TokenNameFinderModel train() throws IOException {
        ObjectStream<NameSample> sampleStream = getStream();
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

    private void evaluate(TokenNameFinderModel model) throws IOException {
        ObjectStream<NameSample> sampleStream = getStream();

        TokenNameFinderEvaluator evaluator = new TokenNameFinderEvaluator(new NameFinderME(model));
        evaluator.evaluate(sampleStream);

        FMeasure result = evaluator.getFMeasure();

        System.out.println(result.toString());
    }
}
