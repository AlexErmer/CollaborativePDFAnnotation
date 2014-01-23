package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.model.EvaluationBean;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class EvaluationAction extends AbstractAction {

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        HttpSession session = request.getSession();

        Map<String, NameFinderResult> resultMap = (Map<String, NameFinderResult>) session.getAttribute("resultMap");
        //TODO: filter exisiting annotations?!

        EvaluationBean evaluationBean = new EvaluationBean();
        evaluationBean.setResultMap(resultMap);
        request.setAttribute("evaluationBean", evaluationBean);

        return "evaluation";
    }
}