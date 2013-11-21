package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.model.EvaluationBean;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class EvaluationAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MessageUtil mu = (MessageUtil) request.getSession().getAttribute(MessageUtil.NAME);

        Map<String, NameFinderResult> resultMap = (Map<String, NameFinderResult>) request.getSession().getAttribute("resultMap");
        //request.getSession().removeAttribute("resultMap");

        EvaluationBean evaluationBean = new EvaluationBean();
        evaluationBean.setResultMap(resultMap);
        request.setAttribute("evaluationBean", evaluationBean);

        return "evaluation";
    }
}