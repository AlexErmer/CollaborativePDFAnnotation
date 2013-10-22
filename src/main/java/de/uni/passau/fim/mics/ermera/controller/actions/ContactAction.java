package de.uni.passau.fim.mics.ermera.controller.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContactAction implements Action {
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return "contact";
    }
}
