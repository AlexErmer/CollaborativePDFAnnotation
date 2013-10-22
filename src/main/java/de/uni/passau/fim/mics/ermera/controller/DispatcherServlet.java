package de.uni.passau.fim.mics.ermera.controller;

import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "Dispatcher", urlPatterns = "/pages/*")
public class DispatcherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response);
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "";
        try {
            Action action = ActionFactory.getAction(request);
            if (action != null) {
                view = action.execute(request, response);
            }

            if (view != null) {
                if (view.equals(request.getPathInfo().substring(1))) {
                    request.getRequestDispatcher("/WEB-INF/" + view + ".jsp").forward(request, response);
                } else {
                    response.sendRedirect(view);
                }
            }
        } catch (Exception e) {
            throw new ServletException("Executing action failed.", e);
        }
    }
}