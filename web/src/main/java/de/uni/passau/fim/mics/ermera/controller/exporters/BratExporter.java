package de.uni.passau.fim.mics.ermera.controller.exporters;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.*;

public class BratExporter implements Exporter {

    public String getRedirectURL(String userid, String fileid) {
        return "../brat/brat/index.xhtml#/" + userid + "/" + fileid;
    }

    public boolean export(String userid, DocumentBean documentBean) throws ExportException {
        try {
            String path = PropertyReader.DATA_PATH + PropertyReader.BRATFOLDER + userid + "/";

            // copy file with utf-8 encoding
            try (Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path + "/" + documentBean.getId() + ".txt"), "UTF-8"))) {
                String str = StringEscapeUtils.unescapeHtml(documentBean.toString());
                out.write(str);
            }

            // create seperate annotation file
            File annFile = new File(path + "/" + documentBean.getId() + ".ann");
            if (!annFile.exists() && !annFile.createNewFile()) {
                throw new ExportException("Annotationfile could not have been saved");
            }

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
            source.close();
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
        try (OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = source.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }

    }
}
