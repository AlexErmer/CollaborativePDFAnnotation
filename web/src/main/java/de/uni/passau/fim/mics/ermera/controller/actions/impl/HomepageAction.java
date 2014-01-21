package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.controller.actions.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomepageAction implements Action {
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "homepage";
    }
}
