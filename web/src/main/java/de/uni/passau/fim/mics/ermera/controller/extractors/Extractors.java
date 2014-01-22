package de.uni.passau.fim.mics.ermera.controller.extractors;


import org.apache.log4j.Logger;

public enum Extractors {
    KNOWMINER(KnowminerExtractor.class);

    private static final Logger LOGGER = Logger.getLogger(Extractors.class);
    private Class cl;

    Extractors(Class cl) {
        this.cl = cl;
    }

    public Extractor getInstance() {
        try {
            return (Extractor) cl.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Instanciation of Exporters Enum failed", e);
        }
        return null;
    }
}
