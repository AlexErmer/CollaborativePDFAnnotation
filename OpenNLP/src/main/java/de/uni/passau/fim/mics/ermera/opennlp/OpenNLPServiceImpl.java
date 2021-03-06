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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OpenNLPServiceImpl implements OpenNLPService {

    //TODO: refactor for loose couling.. its now tied wirh brat
    private ObjectStream<NameSample> getStream(String userid) throws IOException {
        return StreamFactoryRegistry.getFactory(NameSample.class, "brat")
                .create(new String[]{
                        "-bratDataDir", PropertyReader.DATA_PATH + PropertyReader.BRATFOLDER + userid
                        , "-annotationConfig", PropertyReader.DATA_PATH + PropertyReader.BRATFOLDER + userid + "\\annotation.conf"
                        //,"-recursive", "false"
                        //,"-sentenceDetectorModel", ""
                        //,"-tokenizerModel", ""
                        , "-ruleBasedTokenizer", "simple"
                });
    }

    /**
     * This method trains and returns a new {@code TokenNameFinderModel}.
     *
     * @param userid     Id of the user which creates the model.
     * @return the newly generated {@code TokenNameFinderModel}
     * @throws IOException exception for not being able to read files.
     */
    @Override
    public TokenNameFinderModel train(String userid) throws IOException {
        ObjectStream<NameSample> sampleStream = getStream(userid);
        TokenNameFinderModel model;

        try {
            model = NameFinderME.train("en", null, sampleStream, Collections.<String, Object>emptyMap());
        } finally {
            sampleStream.close();
        }

        return model;
    }

    /**
     * This model uses a {@code TokenNameFinderModel} to find entities in the given documents.
     * The documents are encoded in a {@code Map} with its key and value typed as @{code String}.
     * The key of the map is the document identifier and the value is the complete text of the document. <br />
     * It returns a {@code Map} which key is typed @{code String} and its value typed as @{code NameFinderResult}.
     * The key is again the document identifier and the value is an object which contains the tokens and the found entities.
     *
     * @param model        The model which should be used.
     * @param documentStrs The Map of documents in which should be searched
     * @return a map which contains the found entities for each document.
     */
    @Override
    public List<NameFinderResult> find(TokenNameFinderModel model, Map<String, String> documentStrs) throws NLPException {
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

        List<NameFinderResult> results = new ArrayList<>();
        for (Map.Entry<String, String> docStr : documentStrs.entrySet()) {

            final String docName = docStr.getKey();
            final String docText = docStr.getValue();

            Span[] sentencePositions = sentenceDetector.sentPosDetect(docText);
            String[] sentenceTexts = sentenceDetector.sentDetect(docText);

            List<NameFinderResult.Sentence> sentenceList = new ArrayList<>();
            for (int i = 0; i < sentencePositions.length; i++) {

                final Span[] tokenPositions = tokenizer.tokenizePos(sentenceTexts[i]);
                final String[] tokenTexts = tokenizer.tokenize(sentenceTexts[i]);

                Span[] findings = nameFinderME.find(tokenTexts);
                if (findings.length > 0) {
                    List<NameFinderResult.Token> tokenList = new ArrayList<>();
                    for (int j = 0; j < tokenPositions.length; j++) {
                        tokenList.add(new NameFinderResult.Token(tokenPositions[j], tokenTexts[j]));
                    }
                    List<NameFinderResult.Finding> findingsList = new ArrayList<>();
                    for (Span finding : findings) {
                        String text = extractFindingText(finding, tokenList);
                        findingsList.add(new NameFinderResult.Finding(finding, text, finding.getType()));
                    }

                    sentenceList.add(new NameFinderResult.Sentence(sentencePositions[i], sentenceTexts[i], tokenList, findingsList));
                }
                nameFinderME.clearAdaptiveData();
            }
            results.add(new NameFinderResult(docName, docText, sentenceList));
        }
        return results;
    }

    private String extractFindingText(Span finding, List<NameFinderResult.Token> tokenList) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = finding.getStart(); i < finding.getEnd(); i++) {
            if (first) {
                first = false;
            } else {
                sb.append(" ");
            }
            sb.append(tokenList.get(i).getText());
        }
        return sb.toString();
    }
}