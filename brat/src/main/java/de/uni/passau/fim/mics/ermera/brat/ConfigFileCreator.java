package de.uni.passau.fim.mics.ermera.brat;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ConfigFileCreator {
    private ConfigFileCreator() {
    }

    public static void createAnnotationConf(String userid, List<String> types) throws BratException {
        String str;
        try {
            str = IOUtils.toString(ConfigFileCreator.class.getResourceAsStream("/annotation.conf"), "UTF-8");
        } catch (IOException e) {
            throw new BratException("IOException loading Entitytypes", e);
        }
        // convert typelist to string of types with newlines
        StringBuilder sb = new StringBuilder();
        for (String type : types) {
            sb.append(type).append(System.lineSeparator());
        }
        // replace the template
        str = str.replaceAll("---REPLACE_THIS---", sb.toString());
        // save the annotation file in brat working dir
        try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(PropertyReader.DATA_PATH + PropertyReader.BRATFOLDER + userid + "\\annotation.conf"))) {
            modelOut.write(str.getBytes());
        } catch (IOException e) {
            throw new BratException("IOException saving annotationfile", e);
        }
    }
}
