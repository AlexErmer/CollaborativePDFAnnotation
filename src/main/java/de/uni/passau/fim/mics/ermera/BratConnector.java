package de.uni.passau.fim.mics.ermera;

import de.uni.passau.fim.mics.ermera.beans.DocumentBean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BratConnector {

    private BratConnector() {
    }

    public static boolean saveForBrat(DocumentBean loadedDocumentBean) throws IOException {
        String path = PropertyReader.DATA_PATH + PropertyReader.BRAT_WORKING_PATH;
        File file = new File(path + "/" + loadedDocumentBean.getId() + ".txt");
        FileWriter f2 = new FileWriter(file, false);
        f2.write(loadedDocumentBean.saveForBrat());
        f2.close();

        File annFile = new File(path + "/" + loadedDocumentBean.getId() + ".ann");
        annFile.createNewFile();

        return true;
    }
}
