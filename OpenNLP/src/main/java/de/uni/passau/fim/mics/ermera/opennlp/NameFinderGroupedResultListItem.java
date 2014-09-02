package de.uni.passau.fim.mics.ermera.opennlp;

import java.util.List;

public class NameFinderGroupedResultListItem implements Comparable {
    private NameFinderResult.Finding finding;
    private List<SingleNameFinderResult> list;

    public NameFinderGroupedResultListItem(NameFinderResult.Finding finding, List<SingleNameFinderResult> list) {
        this.finding = finding;
        this.list = list;
    }

    public NameFinderResult.Finding getFinding() {
        return finding;
    }

    public void setFinding(NameFinderResult.Finding finding) {
        this.finding = finding;
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

        if (size == otherSize) {
            return 0;
        } else if (size < otherSize) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NameFinderGroupedResultListItem)) {
            return false;
        }

        NameFinderGroupedResultListItem that = (NameFinderGroupedResultListItem) o;

        return !(finding != null ? !finding.equals(that.finding) : that.finding != null) && !(list != null ? !list.equals(that.list) : that.list != null);
    }

    @Override
    public int hashCode() {
        int result = finding != null ? finding.hashCode() : 0;
        result = 31 * result + (list != null ? list.hashCode() : 0);
        return result;
    }
}
