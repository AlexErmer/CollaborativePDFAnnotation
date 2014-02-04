package de.uni.passau.fim.mics.ermera.brat;

public class BratException extends Exception {
    public BratException(String message) {
        super(message);
    }

    public BratException(String message, Throwable cause) {
        super(message, cause);
    }
}
