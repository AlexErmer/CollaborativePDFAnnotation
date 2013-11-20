package de.uni.passau.fim.mics.ermera.controller.exporters;


public enum Exporters {
    BRAT(BratExporter.class);

     private Class cl;

    Exporters(Class cl) {
        this.cl = cl;
    }

    public Exporter getInstance() {
        try {
            return (Exporter) cl.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }
}
