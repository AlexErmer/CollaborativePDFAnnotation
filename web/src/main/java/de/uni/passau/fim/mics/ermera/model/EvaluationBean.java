package de.uni.passau.fim.mics.ermera.model;

import de.uni.passau.fim.mics.ermera.opennlp.NameFinderGroupedResultListItem;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;

import java.util.List;

public class EvaluationBean {
    private static final int CONTEXTRANGE = 7;
    private List<NameFinderResult> resultList;
    private List<NameFinderGroupedResultListItem> groupedResultList;

    public List<NameFinderResult> getResultList() {
        return resultList;
    }

    public void setResultList(List<NameFinderResult> resultList) {
        this.resultList = resultList;
    }

    public int getContextRange() {
        return CONTEXTRANGE;
    }

    public List<NameFinderGroupedResultListItem> getGroupedResultList() {
        return groupedResultList;
    }

    public void setGroupedResultList(List<NameFinderGroupedResultListItem> groupedResultList) {
        this.groupedResultList = groupedResultList;
    }
}
