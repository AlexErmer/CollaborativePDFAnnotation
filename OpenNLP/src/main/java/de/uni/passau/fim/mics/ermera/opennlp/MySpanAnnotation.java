package de.uni.passau.fim.mics.ermera.opennlp;

import opennlp.tools.util.Span;

public class MySpanAnnotation {

    private final String id;
    private final String type;
    private final Span span;
    private final String coveredText;

    public MySpanAnnotation(String id, String type, Span span, String coveredText) {
        this.id = id;
        this.type = type;
        this.span = span;
        this.coveredText = coveredText;
    }

  @Override
  public String toString() {
    return id + "\t" + type + " " + span.getStart() + " " + span.getEnd() + "\t" + coveredText;
  }

}

