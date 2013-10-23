package de.uni.passau.fim.mics.ermera.dao;

import java.io.File;

public interface FileDao {
    File load(String id);

    void store(File file);
}
