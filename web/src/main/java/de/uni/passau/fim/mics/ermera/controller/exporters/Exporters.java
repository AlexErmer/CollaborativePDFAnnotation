package de.uni.passau.fim.mics.ermera.controller.exporters;


import org.apache.log4j.Logger;

public enum Exporters {
    BRAT(BratExporter.class);

    private static final Logger LOGGER = Logger.getLogger(Exporters.class);
    private Class cl;

    Exporters(Class cl) {
        this.cl = cl;
    }

    public Exporter getInstance() {
        try {
            return (Exporter) cl.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Instanciation of Exporters Enum failed", e);
        }
        return null;
    }
}
