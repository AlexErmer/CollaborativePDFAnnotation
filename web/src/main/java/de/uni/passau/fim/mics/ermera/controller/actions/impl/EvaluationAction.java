package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.controller.ViewNames;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.model.EvaluationBean;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class EvaluationAction extends AbstractAction {

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        @SuppressWarnings("unchecked")
        List<NameFinderResult> resultList = (List<NameFinderResult>) session.getAttribute("resultList");

        EvaluationBean evaluationBean = new EvaluationBean();
        evaluationBean.setResultList(resultList);
        request.setAttribute("evaluationBean", evaluationBean);

        return ViewNames.EVALUATION;
    }
}