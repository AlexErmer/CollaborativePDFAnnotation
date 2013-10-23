package de.uni.passau.fim.mics.ermera.dao;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;

import java.io.File;

public class FileDaoImpl implements FileDao {

    @Override
    public File load(String id) {
        return new File(PropertyReader.DATA_PATH + PropertyReader.UPLOAD_PATH + id + ".pdf");
    }

    @Override
    public void store(File file) {

    }
}
