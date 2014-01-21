package de.uni.passau.fim.mics.ermera.opennlp;

import opennlp.tools.util.Span;

public class NameFinderResult {
    private String[] tokens;
    private Span[] nameSpans;

    public NameFinderResult(String[] tokens, Span[] nameSpans) {
        this.tokens = tokens.clone();
        this.nameSpans = nameSpans.clone();
    }

    public String[] getTokens() {
        return tokens;
    }

    public Span[] getNameSpans() {
        return nameSpans;
    }
}
