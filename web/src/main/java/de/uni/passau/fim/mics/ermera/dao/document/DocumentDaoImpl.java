package de.uni.passau.fim.mics.ermera.dao.document;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import opennlp.tools.namefind.TokenNameFinderModel;

import java.io.*;

public class DocumentDaoImpl implements DocumentDao {
    @Override
    public DocumentBean loadDocumentBean(String userid, String id) throws IOException, ClassNotFoundException {
        InputStream fis = new FileInputStream(PropertyReader.DATA_PATH + userid + "\\" +PropertyReader.STORAGE_PATH + "document_" + id);
        ObjectInputStream in = new ObjectInputStream(fis);
        DocumentBean documentBean = (DocumentBean) in.readObject();
        fis.close();

        documentBean.createLines(); // create lines, because they are not serialized
        return documentBean;
    }

    @Override
    public void storeDocumentBean(String userid, DocumentBean documentBean) throws IOException {
        OutputStream fos = new FileOutputStream(PropertyReader.DATA_PATH + userid + "\\" +PropertyReader.STORAGE_PATH + "document_" + documentBean.getId());
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(documentBean);
        fos.close();
    }

    @Override
    public TokenNameFinderModel loadModel(String userid, String id) throws IOException, ClassNotFoundException {
        InputStream fis = new FileInputStream(PropertyReader.DATA_PATH + userid + "\\" +PropertyReader.MODEL_PATH + "model_" + id);
        TokenNameFinderModel model = new TokenNameFinderModel(fis);
        fis.close();

        return model;
    }

    @Override
    public void storeModel(String userid, TokenNameFinderModel model) throws IOException {
        OutputStream modelOut = null;
        try {
            modelOut = new BufferedOutputStream(new FileOutputStream(PropertyReader.DATA_PATH + userid + "\\" +PropertyReader.MODEL_PATH + "model_" + model.hashCode()));
            model.serialize(modelOut);
        } finally {
            if (modelOut != null)
                modelOut.close();
        }
    }
}
