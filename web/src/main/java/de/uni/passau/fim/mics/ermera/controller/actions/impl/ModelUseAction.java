package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.dao.content.ContentRepositoryDao;
import de.uni.passau.fim.mics.ermera.dao.content.ContentRepositoryDaoImpl;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.IndexBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModelUseAction extends AbstractAction {

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        ContentRepositoryDao contentRepositoryDao = new ContentRepositoryDaoImpl();
        DocumentDao documentDao = new DocumentDaoImpl();

        IndexBean indexBean = new IndexBean();
        indexBean.setFileIds(contentRepositoryDao.getAllFileIDs(userid));
        indexBean.setModels(documentDao.loadAllModels(userid));

        request.setAttribute("indexBean", indexBean);
        return "modelUse";
    }
}