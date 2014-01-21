package de.uni.passau.fim.mics.ermera.controller.exporters;

import de.uni.passau.fim.mics.ermera.model.DocumentBean;

public interface Exporter {
    boolean export(String userid, DocumentBean documentBean) throws ExportException;
    String getRedirectURL(String userid, String fileid);
}
