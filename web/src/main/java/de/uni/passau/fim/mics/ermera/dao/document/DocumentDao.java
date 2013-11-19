package de.uni.passau.fim.mics.ermera.dao.document;

import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import opennlp.tools.namefind.TokenNameFinderModel;

import java.io.IOException;

public interface DocumentDao {
    DocumentBean loadDocumentBean(String userid, String id) throws IOException, ClassNotFoundException;
    void storeDocumentBean(String userid, DocumentBean documentBean) throws IOException;

    TokenNameFinderModel loadModel(String userid, String id) throws IOException, ClassNotFoundException;
    void storeModel(String userid, TokenNameFinderModel tokenNameFinderModel) throws IOException;
}
