package de.uni.passau.fim.mics.ermera.controller.actions.impl.doc;

import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ToggleHeadlineAction extends DocumentModificationAction {
    @Override
    protected void doCustomAction(HttpServletRequest request, HttpServletResponse response, DocumentBean documentBean, int pageNumber) {
        documentBean.toggleHeadline(pageNumber, request.getParameter("item"));
    }
}