package de.uni.passau.fim.mics.ermera;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TestentryAction implements Action {
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        session.setAttribute("output", "blala");
        return "testentry";
    }
}
