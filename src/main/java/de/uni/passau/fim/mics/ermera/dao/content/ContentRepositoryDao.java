package de.uni.passau.fim.mics.ermera.dao.content;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface ContentRepositoryDao {

    List<String> getAllFileIDs();

    File load(String id);

    void store(String id, InputStream file) throws ContentRepositoryException;
}
