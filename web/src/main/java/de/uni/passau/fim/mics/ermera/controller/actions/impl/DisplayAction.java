package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.controller.ViewNames;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.DocumentBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class DisplayAction extends AbstractAction {

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        DocumentDao documentDao = new DocumentDaoImpl();
        DocumentBean documentBean = (DocumentBean) session.getAttribute("documentBean");
        if (documentBean == null) {
            mu.addMessage(MessageTypes.ERROR, "No loaded document");
            return null;
        }

        request.setAttribute("hasAnnotationWarning", documentDao.hasAnnotations(userid, documentBean.getId()));

        // get next documentbeanID
        boolean getNext = false;
        Map<String, Boolean> map = documentDao.loadPDFFiles(userid);
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            if (getNext) {
                request.setAttribute("nextDocumentbeanID", entry.getKey());
                break;
            }
            if (entry.getKey().equals(documentBean.getId())) {
                getNext = true;
            }
        }

        return ViewNames.DISPLAY;
    }
}