package de.uni.passau.fim.mics.ermera.controller.actions.impl;

public enum Views {
    HOMEPAGE("homepage");

    private String viewName;

    Views(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
