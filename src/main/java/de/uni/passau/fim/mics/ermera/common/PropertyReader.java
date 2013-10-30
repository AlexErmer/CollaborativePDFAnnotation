package de.uni.passau.fim.mics.ermera.common;

import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

    public static final String APPLICATION_URI;
    public static final String MENDELEY_API_KEY;
    public static final String MENDELEY_SECRET_KEY;
    public static final String UPLOAD_PATH;
    public static final String STORAGE_PATH;
    public static final String BRAT_WORKING_PATH;

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
        UPLOAD_PATH = p.getProperty("UPLOAD_PATH");
        STORAGE_PATH = p.getProperty("STORAGE_PATH");
        BRAT_WORKING_PATH = p.getProperty("BRAT_WORKING_PATH");
    }
}
