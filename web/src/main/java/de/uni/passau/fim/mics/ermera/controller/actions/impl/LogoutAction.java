package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        MessageUtil mu = (MessageUtil) session.getAttribute(MessageUtil.NAME);

        session.removeAttribute("service");
        session.removeAttribute("profile");

        mu.addMessage(MessageTypes.SUCCESS, "Logout successful");

        return "login";
    }
}