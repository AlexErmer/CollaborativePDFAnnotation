package de.uni.passau.fim.mics.ermera.dao.document;

import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import opennlp.tools.namefind.TokenNameFinderModel;

import java.io.IOException;
import java.util.List;

public interface DocumentDao {
    DocumentBean loadDocumentBean(String userid, String id) throws IOException, ClassNotFoundException;
    void storeDocumentBean(String userid, DocumentBean documentBean) throws IOException;

    TokenNameFinderModel loadModel(String userid, String name) throws IOException, ClassNotFoundException;
    public List<String> loadAllModels(String userid);
    void storeModel(String userid, String name, TokenNameFinderModel tokenNameFinderModel) throws IOException;
}
