package de.uni.passau.fim.mics.ermera.opennlp;

public class NameFinderGroupedResultListItem {
    private String document;
    private NameFinderResult.Sentence sentence;
    private NameFinderResult.Finding finding;

    public NameFinderGroupedResultListItem(String document, NameFinderResult.Sentence sentence, NameFinderResult.Finding finding) {
        this.document = document;
        this.sentence = sentence;
        this.finding = finding;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public NameFinderResult.Sentence getSentence() {
        return sentence;
    }

    public void setSentence(NameFinderResult.Sentence sentence) {
        this.sentence = sentence;
    }

    public NameFinderResult.Finding getFinding() {
        return finding;
    }

    public void setFinding(NameFinderResult.Finding finding) {
        this.finding = finding;
    }
}
