package de.uni.passau.fim.mics.ermera.model;

import java.util.List;

public class IndexBean {
    private List<String> fileIds;
    private List<String> models;

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }

    public List<String> getModels() {
        return models;
    }

    public void setModels(List<String> models) {
        this.models = models;
    }
}
