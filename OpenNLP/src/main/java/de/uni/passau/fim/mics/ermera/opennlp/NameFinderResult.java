package de.uni.passau.fim.mics.ermera.opennlp;

import opennlp.tools.util.Span;

import java.util.List;

public class NameFinderResult {
    private String[] tokens;
    private Span nameSpans[];

    public NameFinderResult(String[] tokens, Span[] nameSpans) {
        this.tokens = tokens;
        this.nameSpans = nameSpans;
    }

    public String[] getTokens() {
        return tokens;
    }

    public Span[] getNameSpans() {
        return nameSpans;
    }
}
