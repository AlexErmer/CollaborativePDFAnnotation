package de.uni.passau.fim.mics.ermera.dao;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContentRepositoryDaoImpl implements ContentRepositoryDao {

    @Override
    public List<String> getAllFileIDs() {
        List<String> ret = new ArrayList<>();

        File dir = new File(PropertyReader.UPLOAD_PATH);
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
    public File load(String id) {
        return new File(PropertyReader.UPLOAD_PATH + id);
    }

    @Override
    public void store(String id, InputStream filecontent) throws ContentRepositoryException {

        // store pdf
        OutputStream outputStream = null;
        try {
            // write the inputStream to a FileOutputStream
            outputStream =
                    new FileOutputStream(new File(PropertyReader.UPLOAD_PATH + id));

            int read;
            byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            throw new ContentRepositoryException("Fehler beim Speichern", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new ContentRepositoryException("Fehler beim Schließen des Speicherstreams", e);
                }
            }
        }
    }
}
