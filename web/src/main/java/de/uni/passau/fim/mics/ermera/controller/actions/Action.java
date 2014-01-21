package de.uni.passau.fim.mics.ermera.controller.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
    String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
