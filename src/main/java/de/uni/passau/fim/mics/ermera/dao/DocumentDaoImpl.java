package de.uni.passau.fim.mics.ermera.dao;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import java.io.*;

public class DocumentDaoImpl implements DocumentDao {
    @Override
    public DocumentBean load(String id) throws IOException, ClassNotFoundException {
        InputStream fis = new FileInputStream(PropertyReader.DATA_PATH + PropertyReader.STORAGE_PATH + "document_" + id);
        ObjectInputStream o = new ObjectInputStream(fis);
        DocumentBean documentBean = (DocumentBean) o.readObject();
        fis.close();

        documentBean.createLines(); // create lines, because they are not serialized
        return documentBean;
    }

    @Override
    public void store(DocumentBean documentBean) throws IOException {
        OutputStream fos = new FileOutputStream(PropertyReader.DATA_PATH + PropertyReader.STORAGE_PATH + "document_" + documentBean.getId());
        ObjectOutputStream o = new ObjectOutputStream(fos);
        o.writeObject(documentBean);
        fos.close();
    }
}
