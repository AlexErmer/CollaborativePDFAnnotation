package de.uni.passau.fim.mics.ermera.dao;

import de.uni.passau.fim.mics.ermera.common.FileUtil;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import opennlp.tools.namefind.TokenNameFinderModel;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DocumentDaoImpl implements DocumentDao {
    private static final Logger LOGGER = Logger.getLogger(DocumentDaoImpl.class);

    private static final String DOCUMENTPREFIX = "document_";
    private static final String MODELPREFIX = "model_";

    @Override
    public List<String> loadPDFFiles(String userid) {
        List<String> ret = new ArrayList<>();

        File dir = new File(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.UPLOAD_PATH);
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
    public File loadPDF(String userid, String id) {
        return new File(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.UPLOAD_PATH + id);
    }

    @Override
    public void storePDF(String userid, String id, InputStream filecontent) throws DocumentDaoException {
        OutputStream outputStream = null;
        try {
            // write the inputStream to a FileOutputStream
            outputStream =
                    new FileOutputStream(new File(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.UPLOAD_PATH + id));

            int read;
            byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            throw new DocumentDaoException("Fehler beim Speichern", e);
        } finally {
            closeStream(outputStream);
        }
    }

    private void closeStream(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                LOGGER.error("Fehler beim Schließen des Speicherstreams", e);
            }
        }
    }


    @Override
    public DocumentBean loadDocumentBean(String userid, String id) throws DocumentDaoException {
        DocumentBean documentBean;
        try {
            InputStream fis = new FileInputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.STORAGE_PATH + DOCUMENTPREFIX + id);
            ObjectInputStream in = new ObjectInputStream(fis);
            documentBean = (DocumentBean) in.readObject();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new DocumentDaoException("failed to load documentBean", e);
        }

        // create lines, because they are not serialized
        if (documentBean != null) {
            documentBean.createLines();
        }
        return documentBean;
    }

    @Override
    public void storeDocumentBean(String userid, DocumentBean documentBean) throws IOException {
        OutputStream fos = new FileOutputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.STORAGE_PATH + DOCUMENTPREFIX + documentBean.getId());
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(documentBean);
        fos.close();
    }

    @Override
    public TokenNameFinderModel loadModel(String userid, String name) throws IOException {
        InputStream fis = new FileInputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.MODEL_PATH + MODELPREFIX + name);
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
                    ret.add(f.getName().replace(MODELPREFIX, ""));
                }
            }
        }

        return ret;
    }

    @Override
    public void storeModel(String userid, String name, TokenNameFinderModel model) throws IOException {
        //TODO: check modelname already exists
        try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.MODEL_PATH + MODELPREFIX + name))) {
            model.serialize(modelOut);
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
        if (!dir.exists() && !dir.mkdirs()) {
            mu.addMessage(MessageTypes.ERROR, "Directory \"" + path + "\" not created!");
        }
    }
}
