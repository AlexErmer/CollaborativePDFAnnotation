package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.controller.ViewNames;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.IndexBean;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModelCreateAction extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(ModelCreateAction.class);

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        DocumentDao documentDao = new DocumentDaoImpl();

        IndexBean indexBean = new IndexBean();
        indexBean.setFileIds(documentDao.loadPDFFiles(userid));
        try {
            indexBean.setTypeList(documentDao.loadTypeList(userid));
        } catch (DocumentDaoException e) {
            LOGGER.error("Fehler beim lesen der Entitytypen!", e);
            mu.addMessage(MessageTypes.ERROR, "Fehler beim lesen der Entitytypen!");
        }
        request.setAttribute("indexBean", indexBean);

        return ViewNames.MODELCREATE;
    }
}