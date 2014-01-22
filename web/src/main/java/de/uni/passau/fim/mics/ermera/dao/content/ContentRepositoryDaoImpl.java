package de.uni.passau.fim.mics.ermera.dao.content;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContentRepositoryDaoImpl implements ContentRepositoryDao {
    private static final Logger LOGGER = Logger.getLogger(ContentRepositoryDaoImpl.class);

    @Override
    public List<String> getAllFileIDs(String userid) {
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
    public File load(String userid, String id) {
        return new File(PropertyReader.DATA_PATH + userid + "\\" + PropertyReader.UPLOAD_PATH + id);
    }

    @Override
    public void store(String userid, String id, InputStream filecontent) throws ContentRepositoryException {
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
            throw new ContentRepositoryException("Fehler beim Speichern", e);
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
}
