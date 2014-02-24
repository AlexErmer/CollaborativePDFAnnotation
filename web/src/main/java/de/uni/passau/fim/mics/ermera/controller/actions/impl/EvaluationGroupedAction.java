package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.controller.Views;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.model.EvaluationBean;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderGroupedResultListItem;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;
import de.uni.passau.fim.mics.ermera.opennlp.SingleNameFinderResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EvaluationGroupedAction extends AbstractAction {

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        @SuppressWarnings("unchecked")
        List<NameFinderResult> resultList = (List<NameFinderResult>) session.getAttribute("resultList");
        //TODO: filter exisiting annotations?!

        List<NameFinderGroupedResultListItem> groupedResultList = convertResultList(resultList);

        EvaluationBean evaluationBean = new EvaluationBean();
        evaluationBean.setGroupedResultList(groupedResultList);
        request.setAttribute("evaluationBean", evaluationBean);

        session.setAttribute("evaluationBean", evaluationBean);

        return Views.EVALUATIONGROUPED.toString();
    }

    private List<NameFinderGroupedResultListItem> convertResultList(List<NameFinderResult> resultList) {
        List<NameFinderGroupedResultListItem> nameFinderGroupedResultList = new ArrayList<>();

        for (NameFinderResult result : resultList) {
            for (NameFinderResult.Sentence sentence : result.getSentences()) {
                for (NameFinderResult.Finding finding : sentence.getFindingsList()) {
                    addGroupedEntry(result.getDocumentName(), nameFinderGroupedResultList, finding.getText(), sentence, finding);
                }
            }
        }
        Collections.sort(nameFinderGroupedResultList);
        return nameFinderGroupedResultList;
    }

    private void addGroupedEntry(String documentName, List<NameFinderGroupedResultListItem> list, String text, NameFinderResult.Sentence sentence, NameFinderResult.Finding finding) {
        // search for equal findingstring:
        for (NameFinderGroupedResultListItem nameFinderGroupedResultListItem : list) {
            if (nameFinderGroupedResultListItem.getFindingText().equals(text)) {
                // if found add this result to the found entry
                nameFinderGroupedResultListItem.getList().add(new SingleNameFinderResult(documentName, sentence, finding));
                return;
            }
        }

        // if none found, add a new entry to grouped result list
        List<SingleNameFinderResult> newList = new ArrayList<>();
        newList.add(new SingleNameFinderResult(documentName, sentence, finding));
        list.add(new NameFinderGroupedResultListItem(text, newList));
    }
}