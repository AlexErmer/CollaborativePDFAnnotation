package de.uni.passau.fim.mics.ermera.controller;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "Dispatcher", urlPatterns = "/pages/*")
@MultipartConfig(maxFileSize = 10485760L) // 10MB.
public class DispatcherServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DispatcherServlet.class);

    public static final String LAST_VALID_VIEW = "lastValidView";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response);
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = null;
        try {
            // get and execute correct action
            Action action = retrieveAction(request);
            if (action != null) {
                view = action.execute(request, response);
            }

            // fallback if view not found
            if (view == null) {
                view = (String) request.getSession().getAttribute(LAST_VALID_VIEW);
            } else {
                request.getSession().setAttribute(LAST_VALID_VIEW, view);
            }

            // forward or redirect to next view
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

    private Action retrieveAction(HttpServletRequest request) {
        LOGGER.info("Routing: " + request.getMethod() + request.getPathInfo());
        Action action = null;

        String viewName = request.getPathInfo().substring(1).toUpperCase();
        Views view;
        try {
            view = Views.valueOf(viewName);
        } catch (IllegalArgumentException e) {
            MessageUtil mu = (MessageUtil) request.getSession().getAttribute(MessageUtil.NAME);
            mu.addMessage(MessageTypes.ERROR, "View \"" + viewName + "\" not found!");
            LOGGER.error("View not found!");
            return null;
        }
        final Class actionClass = view.getAction(request.getMethod());
        if (actionClass == null) {
            LOGGER.error("Method for Action not defined!");
            return null;
        }

        try {
            action = (Action) actionClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Action could not be instanciated", e);
        }
        return action;
    }
}