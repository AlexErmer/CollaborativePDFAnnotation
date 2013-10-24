package de.uni.passau.fim.mics.ermera.controller.exporters;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BratConnector {

    // private constructor to make class not instancable
    private BratConnector() {}

    public static boolean saveForBrat(DocumentBean documentBean) throws IOException {
        //TODO add versioning?
        String path = PropertyReader.BRAT_WORKING_PATH;
        File file = new File(path + "/" + documentBean.getId() + ".txt");
        FileWriter f2 = new FileWriter(file, false);
        f2.write(documentBean.toString());
        f2.close();

        // create seperate annotation file
        File annFile = new File(path + "/" + documentBean.getId() + ".ann");
        annFile.createNewFile();

        return true;
    }

}
