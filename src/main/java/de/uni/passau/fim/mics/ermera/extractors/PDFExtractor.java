package de.uni.passau.fim.mics.ermera.extractors;

import de.uni.passau.fim.mics.ermera.beans.DocumentBean;
import de.uni.passau.fim.mics.ermera.exceptions.ExtractException;

public interface PDFExtractor {

    /**
     * Extracts all data from a stored PDF and returns a filled {@code DocumentBean}.
     * @param id id of the PDF
     * @return extracted {@code DocumentBean}
     * @throws de.uni.passau.fim.mics.ermera.exceptions.ExtractException any exception while extraction
     */
    public DocumentBean extract(String id) throws ExtractException;
}
