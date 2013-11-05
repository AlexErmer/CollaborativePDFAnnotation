package de.uni.passau.fim.mics.ermera.dao.content;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface ContentRepositoryDao {

    List<String> getAllFileIDs(String userid);

    File load(String userid, String id);

    void store(String userid, String id, InputStream file) throws ContentRepositoryException;
}
