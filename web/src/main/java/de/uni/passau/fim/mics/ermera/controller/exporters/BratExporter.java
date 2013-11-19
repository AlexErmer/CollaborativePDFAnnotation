package de.uni.passau.fim.mics.ermera.controller.exporters;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;

public class BratExporter implements Exporter {

    public boolean export(String userid, DocumentBean documentBean) throws ExportException {
        try {
            String path = PropertyReader.DATA_PATH + PropertyReader.BRAT_WORKING_PATH + userid + "/";

            // copy file with utf-8 encoding
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path + "/" + documentBean.getId() + ".txt"), "UTF-8"));
            try {
                String str = StringEscapeUtils.unescapeHtml(documentBean.toString());
                out.write(str);
            } finally {
                out.close();
            }

            // create seperate annotation file
            File annFile = new File(path + "/" + documentBean.getId() + ".ann");
            annFile.createNewFile();

            // check config files and create them if necessary
            copyConfigFile("annotation.conf", path);
            copyConfigFile("tools.conf", path);

        } catch (IOException e) {
            throw new ExportException("Could not save Text for Brat: " + e.getMessage(), e);
        }

        return true;
    }

    /**
     * Copies a configuration file.
     *
     * @param name Name of the config file.
     * @param path Path to copy the file to.
     * @throws IOException IOException
     */
    private void copyConfigFile(String name, String path) throws IOException {
        if (!(new File(path + name).exists())) {
            InputStream source = BratExporter.class.getResourceAsStream("/" + name);
            File dest = new File(path + name);
            copyFileUsingStream(source, dest);
        }
    }

    /**
     * Copies a file from source to dest.
     *
     * @param source source file
     * @param dest   destination file
     * @throws IOException IOException
     */
    private void copyFileUsingStream(InputStream source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = source;
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}