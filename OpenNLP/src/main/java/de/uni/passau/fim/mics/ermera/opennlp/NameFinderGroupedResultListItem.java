package de.uni.passau.fim.mics.ermera.opennlp;

import java.util.List;

public class NameFinderGroupedResultListItem implements Comparable {
    private String findingText;
    private List<SingleNameFinderResult> list;

    public NameFinderGroupedResultListItem(String findingText, List<SingleNameFinderResult> list) {
        this.findingText = findingText;
        this.list = list;
    }

    public String getFindingText() {
        return findingText;
    }

    public void setFindingText(String findingText) {
        this.findingText = findingText;
    }

    public List<SingleNameFinderResult> getList() {
        return list;
    }

    public void setList(List<SingleNameFinderResult> list) {
        this.list = list;
    }

    @Override
    public int compareTo(Object o) {
        int size = this.getList().size();
        int otherSize = ((NameFinderGroupedResultListItem) o).getList().size();

        if (size == otherSize) return 0;
        else if (size < otherSize) return 1;
        else return -1;
    }
}
