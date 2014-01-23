package de.uni.passau.fim.mics.ermera.controller.actions.impl.doc;

import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import javax.servlet.http.HttpServletRequest;

public class RemoveBlockAction extends DocumentModificationAction {

    @Override
    protected void doCustomAction(HttpServletRequest request, DocumentBean documentBean, int pageNumber) {
        documentBean.unselectBlock(pageNumber, request.getParameter("item"));
    }
}