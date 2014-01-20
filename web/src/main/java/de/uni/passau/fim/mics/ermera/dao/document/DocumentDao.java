package de.uni.passau.fim.mics.ermera.dao.document;

import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import opennlp.tools.namefind.TokenNameFinderModel;

import java.io.IOException;
import java.util.List;

public interface DocumentDao {
    public DocumentBean loadDocumentBean(String userid, String id) throws IOException, ClassNotFoundException;
    public void storeDocumentBean(String userid, DocumentBean documentBean) throws IOException;

    public TokenNameFinderModel loadModel(String userid, String name) throws IOException, ClassNotFoundException;
    public List<String> loadAllModels(String userid);
    public void storeModel(String userid, String name, TokenNameFinderModel tokenNameFinderModel) throws IOException;

    //TODO: nach brat auslagern?!
    public String loadBratFile(String userid, String name) throws IOException;
    public void storeAnnotationFile(String userid, String name, String content) throws IOException;

    public void createUserFolders(String userid, MessageUtil mu);
}
