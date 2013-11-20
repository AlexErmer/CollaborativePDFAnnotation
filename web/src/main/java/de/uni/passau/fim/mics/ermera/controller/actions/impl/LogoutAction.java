package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MessageUtil mu = (MessageUtil) request.getSession().getAttribute(MessageUtil.NAME);

        request.getSession().removeAttribute("accessToken");
        request.getSession().removeAttribute("service");
        request.getSession().removeAttribute("profile");

        mu.addMessage(MessageTypes.SUCCESS, "Logout successful");

        return "login";
    }
}