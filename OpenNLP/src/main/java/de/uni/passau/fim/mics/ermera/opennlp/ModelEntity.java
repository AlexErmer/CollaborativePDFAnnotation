package de.uni.passau.fim.mics.ermera.opennlp;

import opennlp.tools.namefind.TokenNameFinderModel;

public class ModelEntity {
    private TokenNameFinderModel model;
    private String entitytype;

    public ModelEntity() {
    }

    public ModelEntity(TokenNameFinderModel model, String entitytype) {
        this.model = model;
        this.entitytype = entitytype;
    }

    public TokenNameFinderModel getModel() {
        return model;
    }

    public void setModel(TokenNameFinderModel model) {
        this.model = model;
    }

    public String getEntitytype() {
        return entitytype;
    }

    public void setEntitytype(String entitytype) {
        this.entitytype = entitytype;
    }
}
