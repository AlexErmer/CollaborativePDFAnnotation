package de.uni.passau.fim.mics.ermera.controller.exporters;

import de.uni.passau.fim.mics.ermera.model.DocumentBean;

public interface Exporter {
    public boolean export(String userid, DocumentBean documentBean) throws ExportException;
}
