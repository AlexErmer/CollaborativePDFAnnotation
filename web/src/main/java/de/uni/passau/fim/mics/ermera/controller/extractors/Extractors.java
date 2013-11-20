package de.uni.passau.fim.mics.ermera.controller.extractors;


public enum Extractors {
    KNOWMINER(KnowminerExtractor.class);

    private Class cl;

    Extractors(Class cl) {
        this.cl = cl;
    }

    public Extractor getInstance() {
        try {
            return (Extractor) cl.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }
}
