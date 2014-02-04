package de.uni.passau.fim.mics.ermera.common;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
    private static final Logger LOGGER = Logger.getLogger(PropertyReader.class);

    public static final String APPLICATION_URI;
    public static final String MENDELEY_API_KEY;
    public static final String MENDELEY_SECRET_KEY;
    public static final String DATA_PATH;
    public static final String UPLOADFOLDER;
    public static final String STORAGEFOLDER;
    public static final String BRATFOLDER;
    public static final String MODELFOLDER;
    public static final boolean OFFLINELOGIN;

    // private constructor to prevent instanciation
    private PropertyReader() {
    }

    static {
        Properties p = new Properties();
        try {
            p.load(PropertyReader.class.getResourceAsStream("/config.conf"));
        } catch (IOException e) {
            LOGGER.error("Propertyreader could not load Config!", e);
        }

        APPLICATION_URI = p.getProperty("APPLICATION_URI");
        MENDELEY_API_KEY = p.getProperty("MENDELEY_API_KEY");
        MENDELEY_SECRET_KEY = p.getProperty("MENDELEY_SECRET_KEY");
        DATA_PATH = p.getProperty("DATA_PATH");
        UPLOADFOLDER = p.getProperty("UPLOADFOLDER");
        STORAGEFOLDER = p.getProperty("STORAGEFOLDER");
        BRATFOLDER = p.getProperty("BRATFOLDER");
        MODELFOLDER = p.getProperty("MODELFOLDER");
        OFFLINELOGIN = "true".equals(p.getProperty("OFFLINELOGIN"));
    }
}
