package de.uni.passau.fim.mics.ermera.dao.document;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import opennlp.tools.namefind.TokenNameFinderModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDaoImpl implements DocumentDao {
    @Override
    public DocumentBean loadDocumentBean(String userid, String id) throws IOException, ClassNotFoundException {
        InputStream fis = new FileInputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.STORAGE_PATH + "document_" + id);
        ObjectInputStream in = new ObjectInputStream(fis);
        DocumentBean documentBean = (DocumentBean) in.readObject();
        fis.close();

        documentBean.createLines(); // create lines, because they are not serialized
        return documentBean;
    }

    @Override
    public void storeDocumentBean(String userid, DocumentBean documentBean) throws IOException {
        OutputStream fos = new FileOutputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.STORAGE_PATH + "document_" + documentBean.getId());
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(documentBean);
        fos.close();
    }

    @Override
    public TokenNameFinderModel loadModel(String userid, String name) throws IOException, ClassNotFoundException {
        InputStream fis = new FileInputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.MODEL_PATH + "model_" + name);
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
                    ret.add(f.getName());
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
            modelOut = new BufferedOutputStream(new FileOutputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.MODEL_PATH + "model_" + name));
            model.serialize(modelOut);
        } finally {
            if (modelOut != null)
                modelOut.close();
        }
    }
}
