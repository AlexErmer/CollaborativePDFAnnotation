package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.controller.actions.Views;
import de.uni.passau.fim.mics.ermera.model.EvaluationBean;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class EvaluationAction extends AbstractAction {

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        @SuppressWarnings("unchecked")
        Map<String, NameFinderResult> resultMap = (Map<String, NameFinderResult>) session.getAttribute("resultMap");
        //TODO: filter exisiting annotations?!

        EvaluationBean evaluationBean = new EvaluationBean();
        evaluationBean.setResultMap(resultMap);
        request.setAttribute("evaluationBean", evaluationBean);

        return Views.EVALUATION.toString();
    }
}