package de.uni.passau.fim.mics.ermera.opennlp;

import opennlp.tools.namefind.TokenNameFinderModel;

import java.io.IOException;
import java.util.Map;

public interface OpenNLPService {
    TokenNameFinderModel train(String userid, String entityName) throws IOException;

    Map<String, NameFinderResult> find(TokenNameFinderModel model, Map<String, String> documentStrs) throws NLPException;
}
