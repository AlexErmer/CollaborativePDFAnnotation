package de.uni.passau.fim.mics.ermera.opennlp;

import opennlp.tools.util.Span;

import java.util.ArrayList;
import java.util.List;

public class NameFinderResult {
    private String documentName;
    private String documentText;
    private List<Sentence> sentences;

    public NameFinderResult(NameFinderResult nameFinderResult) {
        documentName = nameFinderResult.getDocumentName();
        documentText = nameFinderResult.getDocumentText();

        sentences = new ArrayList<>();
        for (Sentence sentence : nameFinderResult.getSentences()) {
            sentences.add(new Sentence(sentence));
        }
    }

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

    public static class Sentence implements Cloneable {
        private Span position;
        private String text;
        private List<Token> tokens;
        private List<Finding> findingsList;

        public Sentence(Sentence sentence) {
           position = sentence.getPosition();
            text = sentence.getText();
            tokens = new ArrayList<>();
         for (Token token : sentence.getTokens()) {
            tokens.add(new Token(token));
        }
            findingsList = new ArrayList<>();
        for (Finding finding : sentence.getFindingsList()) {
            findingsList.add(new Finding(finding));
        }
        }

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

    public static class Finding implements Cloneable {
        private Span span;
        private String text;
        private String type;

        public Finding(Finding finding) {
            span = new Span(finding.getSpan(), 0);
            text = finding.getText();
            type = finding.getType();
        }

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

    public static class Token implements Cloneable {
        private Span position;
        private String text;

        public Token(Token token) {
            position = token.getPosition();
            text = token.getText();
        }

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
