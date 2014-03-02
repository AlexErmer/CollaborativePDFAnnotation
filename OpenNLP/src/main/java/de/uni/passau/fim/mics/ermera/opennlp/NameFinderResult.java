package de.uni.passau.fim.mics.ermera.opennlp;

import opennlp.tools.util.Span;

import java.util.List;

public class NameFinderResult {
    private String documentName;
    private String documentText;
    private List<Sentence> sentences;


    public NameFinderResult(String documentName, String documentText, List<Sentence> sentences) {
        this.documentName = documentName;
        this.documentText = documentText;
        this.sentences = sentences;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentText() {
        return documentText;
    }

    public void setDocumentText(String documentText) {
        this.documentText = documentText;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public static class Sentence {
        private Span position;
        private String text;
        private List<Token> tokens;
        private List<NameFinderResult.Finding> findingsList;

        public Sentence(Span position, String text, List<Token> tokens, List<Finding> findingsList) {
            this.position = position;
            this.text = text;
            this.tokens = tokens;
            this.findingsList = findingsList;
        }

        public Span getPosition() {
            return position;
        }

        public void setPosition(Span position) {
            this.position = position;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<Token> getTokens() {
            return tokens;
        }

        public void setTokens(List<Token> tokens) {
            this.tokens = tokens;
        }

        public List<Finding> getFindingsList() {
            return findingsList;
        }

        public void setFindingsList(List<Finding> findingsList) {
            this.findingsList = findingsList;
        }
    }

    public static class Finding {
        private Span span;
        private String text;
        private String type;

        public Finding(Span span, String text, String type) {
            this.span = span;
            this.text = text;
            this.type = type;
        }

        public Span getSpan() {
            return span;
        }

        public void setSpan(Span span) {
            this.span = span;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Token {
        private Span position;
        private String text;

        public Token(Span position, String text) {
            this.position = position;
            this.text = text;
        }

        public Span getPosition() {
            return position;
        }

        public void setPosition(Span position) {
            this.position = position;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
