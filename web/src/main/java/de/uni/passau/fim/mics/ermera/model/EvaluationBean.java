package de.uni.passau.fim.mics.ermera.model;

import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;

import java.util.Map;

public class EvaluationBean {
    private int contextRange = 7;
    private Map<String, NameFinderResult> resultMap;

    public Map<String, NameFinderResult> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, NameFinderResult> resultMap) {
        this.resultMap = resultMap;
    }

    public int getContextRange() {
        return contextRange;
    }

    public void setContextRange(int contextRange) {
        this.contextRange = contextRange;
    }
}
