package de.uni.passau.fim.mics.ermera.controller.actions.impl.doc;

import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import javax.servlet.http.HttpServletRequest;

public class ToggleNewParagraphAction extends DocumentModificationAction {

    @Override
    protected void doCustomAction(HttpServletRequest request, DocumentBean documentBean, int pageNumber) {
        documentBean.toggleNewParagraph(pageNumber, request.getParameter("item"));
    }
}