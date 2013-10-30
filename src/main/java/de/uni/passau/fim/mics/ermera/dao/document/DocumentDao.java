package de.uni.passau.fim.mics.ermera.dao.document;

import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import java.io.IOException;

public interface DocumentDao {
    DocumentBean load(String id) throws IOException, ClassNotFoundException;

    void store(DocumentBean documentBean) throws IOException;
}
