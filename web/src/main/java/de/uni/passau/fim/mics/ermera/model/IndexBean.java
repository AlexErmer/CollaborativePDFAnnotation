package de.uni.passau.fim.mics.ermera.model;

import java.util.List;
import java.util.Map;

public class IndexBean {
    private String selectedFile;
    private Map<String, Boolean> fileIds;
    private List<String> models;
    private List<String> typeList;

    public String getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(String selectedFile) {
        this.selectedFile = selectedFile;
    }

    public Map<String, Boolean> getFileIds() {
        return fileIds;
    }

    public void setFileIds(Map<String, Boolean> fileIds) {
        this.fileIds = fileIds;
    }

    public List<String> getModels() {
        return models;
    }

    public void setModels(List<String> models) {
        this.models = models;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }
}
