package de.uni.passau.fim.mics.ermera.controller.extractors;

import de.uni.passau.fim.mics.ermera.model.DocumentBean;

public interface Extractor {

    /**
     * Extracts all data from a stored PDF and returns a filled {@code DocumentBean}.
     * @param id id of the PDF
     * @return extracted {@code DocumentBean}
     * @throws ExtractException any exception while extraction
     */
    public DocumentBean extract(String id) throws ExtractException;
}
