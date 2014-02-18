package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.controller.Views;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.model.EvaluationBean;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderGroupedResultListItem;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class EvaluationGroupedAction extends AbstractAction {

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        @SuppressWarnings("unchecked")
        Map<String, NameFinderResult> resultMap = (Map<String, NameFinderResult>) session.getAttribute("resultMap");
        //TODO: filter exisiting annotations?!

        Map<String, List<NameFinderGroupedResultListItem>> groupedResultMap = convertResultMap(resultMap);

        EvaluationBean evaluationBean = new EvaluationBean();
        evaluationBean.setGroupedResultMap(groupedResultMap);
        request.setAttribute("evaluationBean", evaluationBean);

        return Views.EVALUATIONGROUPED.toString();
    }

    private Map<String, List<NameFinderGroupedResultListItem>> convertResultMap(Map<String, NameFinderResult> resultMap) {
        Map<String, List<NameFinderGroupedResultListItem>> nameFinderGroupedResultMap = new HashMap<>();

        for (Map.Entry<String, NameFinderResult> entry : resultMap.entrySet()) {

            for (NameFinderResult.Sentence sentence : entry.getValue().getSentences()) {
                for (NameFinderResult.Finding finding : sentence.getFindingsList()) {
                    addGroupedEntry(nameFinderGroupedResultMap, finding.getText(), entry.getKey(), sentence, finding);
                }
            }
        }

        // order map..
        nameFinderGroupedResultMap = sortByComparator(nameFinderGroupedResultMap);

        return nameFinderGroupedResultMap;
    }

    private void addGroupedEntry(Map<String, List<NameFinderGroupedResultListItem>> map, String text, String document, NameFinderResult.Sentence sentence, NameFinderResult.Finding finding) {
        NameFinderGroupedResultListItem nameFinderGroupedResultListItem = new NameFinderGroupedResultListItem(document, sentence, finding);

        List<NameFinderGroupedResultListItem> list;
        if (map.containsKey(text)) {
            list = map.get(text);
        } else {
            list = new ArrayList<>();
            map.put(text, list);
        }
        list.add(nameFinderGroupedResultListItem);
    }


    private static Map<String, List<NameFinderGroupedResultListItem>> sortByComparator(Map<String, List<NameFinderGroupedResultListItem>> unsortMap) {

        List<Map.Entry<String,List<NameFinderGroupedResultListItem>>> list = new LinkedList<>(unsortMap.entrySet());

        // sort list based on comparator
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((List)((Map.Entry) (o2)).getValue()).size())
                                       .compareTo(((List)((Map.Entry) (o1)).getValue()).size());
            }
        });
        Collections.reverseOrder();

        // put sorted list into map again
        //LinkedHashMap make sure order in which keys were inserted
        Map<String, List<NameFinderGroupedResultListItem>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, List<NameFinderGroupedResultListItem>> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}