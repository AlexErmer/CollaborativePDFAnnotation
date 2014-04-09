package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import de.uni.passau.fim.mics.ermera.controller.Views;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.IndexBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class AnnotateAction extends AbstractAction {

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        DocumentDao documentDao = new DocumentDaoImpl();
        IndexBean indexBean = new IndexBean();

        String fileId = request.getParameter("fileid");
        if (fileId != null) {
            indexBean.setSelectedFile(fileId);
        }


        // get next documentbeanID
        boolean getNext = false;
        Map<String, Boolean> map = documentDao.loadPDFFiles(userid);
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            if (getNext) {
                request.setAttribute("nextDocumentbeanID", entry.getKey());
                break;
            }
            if (entry.getKey().equals(fileId)) {
                getNext = true;
            }
        }

        indexBean.setFileIds(documentDao.loadPDFFiles(userid));
        request.setAttribute("indexBean", indexBean);
        return Views.ANNOTATE.toString();
    }
}