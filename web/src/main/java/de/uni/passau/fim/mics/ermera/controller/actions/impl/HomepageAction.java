package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.dao.content.ContentRepositoryDao;
import de.uni.passau.fim.mics.ermera.dao.content.ContentRepositoryDaoImpl;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.IndexBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomepageAction implements Action {
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Profile profile = (Profile) request.getSession().getAttribute("profile");
        String userid = profile.getMain().getProfileId();

        ContentRepositoryDao contentRepositoryDao = new ContentRepositoryDaoImpl();
        DocumentDao documentDao = new DocumentDaoImpl();

        IndexBean indexBean = new IndexBean();
        indexBean.setFileIds(contentRepositoryDao.getAllFileIDs(userid));
        indexBean.setModels(documentDao.loadAllModels(userid));

        request.setAttribute("indexBean", indexBean);
        request.getSession().removeAttribute("documentBean");

        return "homepage";
    }
}
