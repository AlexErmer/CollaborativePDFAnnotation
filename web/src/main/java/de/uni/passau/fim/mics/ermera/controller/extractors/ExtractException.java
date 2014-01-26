package de.uni.passau.fim.mics.ermera.controller.extractors;

public class ExtractException extends Exception {

    public ExtractException(String message) {
        super(message);
    }

    public ExtractException(String message, Throwable cause) {
        super(message, cause);
    }
}
