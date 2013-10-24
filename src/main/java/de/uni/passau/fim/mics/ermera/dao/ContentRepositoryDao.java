package de.uni.passau.fim.mics.ermera.dao;

import java.io.File;

public interface ContentRepositoryDao {
    File load(String id);

    void store(File file);
}
