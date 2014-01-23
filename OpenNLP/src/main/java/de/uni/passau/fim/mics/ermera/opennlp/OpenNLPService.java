package de.uni.passau.fim.mics.ermera.opennlp;

import opennlp.tools.namefind.TokenNameFinderModel;

import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Alexander
 * Date: 23.01.14
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
public interface OpenNLPService {
    TokenNameFinderModel train(String userid, String entityName) throws IOException;

    Map<String, NameFinderResult> find(TokenNameFinderModel model, Map<String, String> documentStrs) throws NLPException;
}
