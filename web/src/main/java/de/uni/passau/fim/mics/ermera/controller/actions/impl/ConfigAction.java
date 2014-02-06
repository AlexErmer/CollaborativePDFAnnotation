package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.controller.Views;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.ConfigBean;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ConfigAction extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(ConfigAction.class);

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        ConfigBean configBean = new ConfigBean();
        DocumentDao documentDao = new DocumentDaoImpl();
        List<String> typeList = null;

        // load typelist
        try {
            typeList = documentDao.loadTypeList(userid);
        } catch (DocumentDaoException e) {
            LOGGER.error("Error while loading Typelist", e);
        }

        // handle new and delete actions
        handleAddNewType(request, documentDao, typeList);
        handleDeleteType(request, documentDao, typeList);

        // provide current list to viewbean
        configBean.setTypes(typeList);
        request.setAttribute("configBean", configBean);
        return Views.CONFIG.toString();
    }

    private void handleDeleteType(HttpServletRequest request, DocumentDao documentDao, List<String> typeList) {
        String delete = request.getParameter("delete");
        if (delete != null && typeList != null) {
            typeList.remove(delete);
            saveTypeList(documentDao, typeList);
        }
    }

    private void handleAddNewType(HttpServletRequest request, DocumentDao documentDao, List<String> typeList) {
        String newType = request.getParameter("newType");
        if (newType != null && typeList != null) {
            typeList.add(newType);
            saveTypeList(documentDao, typeList);
        }
    }

    private void saveTypeList(DocumentDao documentDao, List<String> typeList) {
        try {
            documentDao.saveTypeList(userid, typeList);
        } catch (DocumentDaoException e) {
            LOGGER.error("Types-Datei konnte nicht gespeichert werden.", e);
            mu.addMessage(MessageTypes.ERROR, "Types konnte nicht gespeichert werden");
        }
    }
}
