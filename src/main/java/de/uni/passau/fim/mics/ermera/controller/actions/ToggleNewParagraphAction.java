package de.uni.passau.fim.mics.ermera.controller.actions;

import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ToggleNewParagraphAction extends DocumentModificationAction {
    @Override
    protected void doCustomAction(HttpServletRequest request, HttpServletResponse response, DocumentBean documentBean, int pageNumber) {
        documentBean.toggleNewParagraph(pageNumber, request.getParameter("item"));
    }
}