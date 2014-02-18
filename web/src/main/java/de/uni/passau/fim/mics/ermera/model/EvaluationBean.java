package de.uni.passau.fim.mics.ermera.model;

import de.uni.passau.fim.mics.ermera.opennlp.NameFinderGroupedResultListItem;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;

import java.util.List;
import java.util.Map;

public class EvaluationBean {
    private static final int contextRange = 7;
    private Map<String, NameFinderResult> resultMap;
    private Map<String, List<NameFinderGroupedResultListItem>> groupedResultMap;

    public Map<String, NameFinderResult> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, NameFinderResult> resultMap) {
        this.resultMap = resultMap;
    }

    public int getContextRange() {
        return contextRange;
    }

    public Map<String, List<NameFinderGroupedResultListItem>> getGroupedResultMap() {
        return groupedResultMap;
    }

    public void setGroupedResultMap(Map<String, List<NameFinderGroupedResultListItem>> groupedResultMap) {
        this.groupedResultMap = groupedResultMap;
    }
}
