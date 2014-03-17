package de.uni.passau.fim.mics.ermera.dao;

import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import opennlp.tools.namefind.TokenNameFinderModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DocumentDao {
    List<String> loadPDFFiles(String userid);
    File loadPDF(String userid, String id);
    void storePDF(String userid, String filename, InputStream file) throws DocumentDaoException;

    DocumentBean loadDocumentBean(String userid, String id) throws DocumentDaoException;
    void storeDocumentBean(String userid, DocumentBean documentBean) throws DocumentDaoException;

    TokenNameFinderModel loadModel(String userid, String name) throws IOException;
    List<String> loadAllModels(String userid);
    void storeModel(String userid, String name, TokenNameFinderModel tokenNameFinderModel) throws IOException;

    //TODO: nach brat auslagern?!
    String loadBratFile(String userid, String name) throws IOException;
    void storeAnnotationFile(String userid, String name, String content) throws IOException;

    boolean hasAnnotations(String userid, String id);
    void deleteAnnotationFile(String userid, String id) throws DocumentDaoException;

    void createUserFolders(String userid, MessageUtil mu);

    List<String> loadTypeList(String userid) throws DocumentDaoException;
    void saveTypeList(String userid, List<String> types) throws DocumentDaoException;
}
