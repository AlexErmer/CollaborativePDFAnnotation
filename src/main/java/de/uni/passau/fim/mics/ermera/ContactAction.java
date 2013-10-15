package de.uni.passau.fim.mics.ermera;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ContactAction implements Action {
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return "contact";
    }
}
