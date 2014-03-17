package de.uni.passau.fim.mics.ermera.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.uni.passau.fim.mics.ermera.brat.BratException;
import de.uni.passau.fim.mics.ermera.brat.ConfigFileCreator;
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
    private static final String ENTITYLISTNAME = "entityList.json";

    @Override
    public List<String> loadPDFFiles(String userid) {
        List<String> ret = new ArrayList<>();

        File dir = new File(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.UPLOADFOLDER);
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
        return new File(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.UPLOADFOLDER + id);
    }

    @Override
    public void storePDF(String userid, String filename, InputStream filecontent) throws DocumentDaoException {
        OutputStream outputStream = null;
        try {
            // clean filename from everything except letters and numbers
            String cleanedFilename = filename.replaceAll("\\s", "_");
            cleanedFilename = cleanedFilename.replaceAll("[^a-zA-Z0-9_\\[\\.pdf\\]]+", "");

            // write the inputStream to a FileOutputStream
            outputStream =
                    new FileOutputStream(new File(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.UPLOADFOLDER + cleanedFilename));

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
            InputStream fis = new FileInputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.STORAGEFOLDER + DOCUMENTPREFIX + id);
            ObjectInputStream in = new ObjectInputStream(fis);
            documentBean = (DocumentBean) in.readObject();
            in.close();
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
    public void storeDocumentBean(String userid, DocumentBean documentBean) throws DocumentDaoException {
        try {
            OutputStream fos = new FileOutputStream(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.STORAGEFOLDER + DOCUMENTPREFIX + documentBean.getId());
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(documentBean);
            out.close();
            fos.close();
        } catch (IOException e) {
            LOGGER.error("IO Error: Could not save DocumentBean", e);
            throw new DocumentDaoException("IO Error: Could not save DocumentBean", e);
        }

        deleteAnnotationFile(userid, documentBean.getId());
    }

    @Override
    public TokenNameFinderModel loadModel(String userid, String name) throws IOException {
        String modelFileName = PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.MODELFOLDER + MODELPREFIX + name;
        InputStream fis = new FileInputStream(modelFileName);
        TokenNameFinderModel model = new TokenNameFinderModel(fis);
        fis.close();

        return model;
    }

    @Override
    public List<String> loadAllModels(String userid) {
        List<String> ret = new ArrayList<>();

        File dir = new File(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.MODELFOLDER);
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
        String modelFileName = PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.MODELFOLDER + MODELPREFIX + name;
        try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFileName))) {
            model.serialize(modelOut);
        }
    }

    @Override
    public String loadBratFile(String userid, String name) throws IOException {
        return FileUtil.readFile(PropertyReader.DATA_PATH + PropertyReader.BRATFOLDER + userid + "\\" + name + ".txt", StandardCharsets.UTF_8);
    }

    @Override
    public void storeAnnotationFile(String userid, String name, String content) throws IOException {
        OutputStream fos = new FileOutputStream(PropertyReader.DATA_PATH + PropertyReader.BRATFOLDER + userid + "\\" + name + ".ann", true); // APPEND!
        fos.write(content.getBytes());
        fos.close();
    }

    @Override
    public boolean hasAnnotations(String userid, String id) {
        File annoFile = new File(PropertyReader.DATA_PATH + PropertyReader.BRATFOLDER + userid + "\\" + id + ".ann");
        return annoFile.length() > 0;
    }

    @Override
    public void deleteAnnotationFile(String userid, String id) throws DocumentDaoException {
        File annoFile = new File(PropertyReader.DATA_PATH + PropertyReader.BRATFOLDER + userid + "\\" + id + ".ann");
        if (annoFile.exists() && !annoFile.delete()) {
            throw new DocumentDaoException("Error while deleting annotationFile");
        }
    }

    @Override
    public void createUserFolders(String userid, MessageUtil mu) {
        createFolder(PropertyReader.UPLOADFOLDER, userid, mu);
        createFolder(PropertyReader.STORAGEFOLDER, userid, mu);
        createFolder(PropertyReader.BRATFOLDER, userid, mu);
        createFolder(PropertyReader.MODELFOLDER, userid, mu);
    }

    private void createFolder(String path, String userid, MessageUtil mu) {
        File dir = new File(PropertyReader.DATA_PATH + userid + "\\" + path);
        if (!dir.exists() && !dir.mkdirs()) {
            mu.addMessage(MessageTypes.ERROR, "Directory \"" + path + "\" not created!");
        }
    }

    @Override
    public List<String> loadTypeList(String userid) throws DocumentDaoException {
        List<String> types;
        try {
            String str = FileUtil.readFile(PropertyReader.DATA_PATH + userid + "\\" + ENTITYLISTNAME);
            types = new Gson().fromJson(str, new TypeToken<List<String>>() {
            }.getType());
        } catch (IOException e) {
            saveTypeList(userid, new ArrayList<String>());
            throw new DocumentDaoException("IOException loading Entitytypes", e);
        }

        return types;
    }

    @Override
    public void saveTypeList(String userid, List<String> types) throws DocumentDaoException {
        // save typelist as json in users path
        try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(PropertyReader.DATA_PATH + userid + "\\" + ENTITYLISTNAME))) {
            String json = new Gson().toJson(types);
            modelOut.write(json.getBytes());
            ConfigFileCreator.createAnnotationConf(userid, types);
        } catch (IOException | BratException e) {
            throw new DocumentDaoException("IOException saving Entitytypes", e);
        }
    }
}
