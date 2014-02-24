package de.uni.passau.fim.mics.ermera.opennlp;

public class SingleNameFinderResult {
    private String documentName;
    private NameFinderResult.Sentence sentence;
    private NameFinderResult.Finding finding;

    public SingleNameFinderResult(String documentName, NameFinderResult.Sentence sentence, NameFinderResult.Finding finding) {
        this.documentName = documentName;
        this.sentence = sentence;
        this.finding = finding;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
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
