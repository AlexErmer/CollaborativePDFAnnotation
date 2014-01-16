package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.model.EvaluationBean;
import de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class EvaluationAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        MessageUtil mu = (MessageUtil) session.getAttribute(MessageUtil.NAME);

        Map<String, NameFinderResult> resultMap = (Map<String, NameFinderResult>) session.getAttribute("resultMap");
        //TODO: filter exisiting annotations?!

        EvaluationBean evaluationBean = new EvaluationBean();
        evaluationBean.setResultMap(resultMap);
        request.setAttribute("evaluationBean", evaluationBean);

        return "evaluation";
    }
}