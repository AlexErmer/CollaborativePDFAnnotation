package de.uni.passau.fim.mics.ermera.controller.exporters;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.*;

public class BratExporter implements Exporter {

    public boolean export(DocumentBean documentBean) throws ExportException {
        try {
            String path = PropertyReader.DATA_PATH + PropertyReader.BRAT_WORKING_PATH;

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
        } catch (IOException e) {
            throw new ExportException("Could not save Text for Brat: " + e.getMessage(), e);
        }

        return true;
    }
}
