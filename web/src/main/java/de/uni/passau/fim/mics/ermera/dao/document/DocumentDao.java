package de.uni.passau.fim.mics.ermera.dao.document;

import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import opennlp.tools.namefind.TokenNameFinderModel;

import java.io.IOException;
import java.util.List;

public interface DocumentDao {
    DocumentBean loadDocumentBean(String userid, String id) throws IOException, ClassNotFoundException;
    void storeDocumentBean(String userid, DocumentBean documentBean) throws IOException;

    TokenNameFinderModel loadModel(String userid, String name) throws IOException, ClassNotFoundException;
    List<String> loadAllModels(String userid);
    void storeModel(String userid, String name, TokenNameFinderModel tokenNameFinderModel) throws IOException;

    //TODO: nach brat auslagern?!
    String loadBratFile(String userid, String name) throws IOException;
    void storeAnnotationFile(String userid, String name, String content) throws IOException;

    void createUserFolders(String userid, MessageUtil mu);
}
