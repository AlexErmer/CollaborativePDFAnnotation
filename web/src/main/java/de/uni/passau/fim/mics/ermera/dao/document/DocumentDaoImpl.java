package de.uni.passau.fim.mics.ermera.dao.document;

import de.uni.passau.fim.mics.ermera.common.FileUtil;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import opennlp.tools.namefind.TokenNameFinderModel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DocumentDaoImpl implements DocumentDao {
    private final String DOCUMENT_PREFIX = "document_";
    private final String MODEL_PREFIX = "model_";

    @Override
    public DocumentBean loadDocumentBean(String userid, String id) throws IOException, ClassNotFoundException {
        InputStream fis = new FileInputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.STORAGE_PATH + DOCUMENT_PREFIX + id);
        ObjectInputStream in = new ObjectInputStream(fis);
        DocumentBean documentBean = (DocumentBean) in.readObject();
        fis.close();

        // create lines, because they are not serialized
        documentBean.createLines();
        return documentBean;
    }

    @Override
    public void storeDocumentBean(String userid, DocumentBean documentBean) throws IOException {
        OutputStream fos = new FileOutputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.STORAGE_PATH + DOCUMENT_PREFIX + documentBean.getId());
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(documentBean);
        fos.close();
    }

    @Override
    public TokenNameFinderModel loadModel(String userid, String name) throws IOException, ClassNotFoundException {
        InputStream fis = new FileInputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.MODEL_PATH + MODEL_PREFIX + name);
        TokenNameFinderModel model = new TokenNameFinderModel(fis);
        fis.close();

        return model;
    }

    @Override
    public List<String> loadAllModels(String userid) {
        List<String> ret = new ArrayList<>();

        File dir = new File(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.MODEL_PATH);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (!f.isDirectory()) {
                    ret.add(f.getName().replace(MODEL_PREFIX, ""));
                }
            }
        }

        return ret;
    }

    @Override
    public void storeModel(String userid, String name, TokenNameFinderModel model) throws IOException {
        //TODO: check modelname already exists
        OutputStream modelOut = null;
        try {
            modelOut = new BufferedOutputStream(new FileOutputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.MODEL_PATH + MODEL_PREFIX + name));
            model.serialize(modelOut);
        } finally {
            if (modelOut != null) {
                modelOut.close();
            }
        }
    }

    @Override
    public String loadBratFile(String userid, String name) throws IOException {
        return FileUtil.readFile(PropertyReader.DATA_PATH + PropertyReader.BRAT_WORKING_PATH + userid + "\\" + name + ".txt", StandardCharsets.UTF_8);
    }

    @Override
    public void storeAnnotationFile(String userid, String name, String content) throws IOException {
        //TODO: versionierung!! anderenfalls werden die annotationfiles ggf. korrupt
        OutputStream fos = new FileOutputStream(PropertyReader.DATA_PATH + PropertyReader.BRAT_WORKING_PATH + userid + "\\" + name + ".ann", true); // APPEND!
        fos.write(content.getBytes());
        fos.close();
    }

    @Override
    public void createUserFolders(String userid, MessageUtil mu) {
        createFolder(PropertyReader.UPLOAD_PATH, userid, mu);
        createFolder(PropertyReader.STORAGE_PATH, userid, mu);
        createFolder(PropertyReader.BRAT_WORKING_PATH, userid, mu);
        createFolder(PropertyReader.MODEL_PATH, userid, mu);
    }

    private void createFolder(String path, String userid, MessageUtil mu) {
        File dir = new File(PropertyReader.DATA_PATH + userid + "\\" + path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                mu.addMessage(MessageTypes.ERROR, "Directory \"" + path + "\" not created!");
            }
        }
    }
}
