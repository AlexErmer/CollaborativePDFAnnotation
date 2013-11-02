package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DisplayAction implements Action {

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        DocumentBean documentBean = (DocumentBean) session.getAttribute("documentBean");
        if (documentBean == null) {
            request.setAttribute("errorMessage", "No loaded document");
            return "";
        }

        return "display";
    }
}