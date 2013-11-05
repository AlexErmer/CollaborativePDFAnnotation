package de.uni.passau.fim.mics.ermera.common;

import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

    public static final String APPLICATION_URI;
    public static final String MENDELEY_API_KEY;
    public static final String MENDELEY_SECRET_KEY;
    public static final String DATA_PATH;
    public static final String UPLOAD_PATH;
    public static final String STORAGE_PATH;
    public static final String BRAT_WORKING_PATH;
    public static final boolean OFFLINELOGIN;

    // private constructor to prevent instanciation
    private PropertyReader() {
    }

    static {
        Properties p = new Properties();
        try {
            p.load(PropertyReader.class.getResourceAsStream("/config.conf"));
        } catch (IOException e) {
            System.out.println("Could not load Config!");
        }

        APPLICATION_URI = p.getProperty("APPLICATION_URI");
        MENDELEY_API_KEY = p.getProperty("MENDELEY_API_KEY");
        MENDELEY_SECRET_KEY = p.getProperty("MENDELEY_SECRET_KEY");
        DATA_PATH = p.getProperty("DATA_PATH");
        UPLOAD_PATH = p.getProperty("UPLOAD_PATH");
        STORAGE_PATH = p.getProperty("STORAGE_PATH");
        BRAT_WORKING_PATH = p.getProperty("BRAT_WORKING_PATH");
        OFFLINELOGIN = p.getProperty("OFFLINELOGIN").equals("true");
    }
}
