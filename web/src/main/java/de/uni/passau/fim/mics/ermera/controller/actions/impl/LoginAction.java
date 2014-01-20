package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.document.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.LoginBean;
import de.uni.passau.fim.mics.ermera.oauth.MendeleyOAuthServiceImpl;
import de.uni.passau.fim.mics.ermera.oauth.MyOAuthService;
import org.scribe.model.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        MessageUtil mu = (MessageUtil) session.getAttribute(MessageUtil.NAME);
        DocumentDao documentDao = new DocumentDaoImpl();

        Token requestToken;
        MyOAuthService oAuthService = new MendeleyOAuthServiceImpl();

        if (PropertyReader.OFFLINELOGIN) {
            Profile profile = oAuthService.getDummyProfile();
            documentDao.createUserFolders("dummyUser", mu);
            session.setAttribute("profile", profile);
            mu.addMessage(MessageTypes.SUCCESS, "Dummy Login successful");
            return "homepage";
        }

        String authcode = request.getParameter("oauth_verifier");
        if (authcode == null) {
            requestToken = oAuthService.getRequestToken();
            session.setAttribute("requestToken", requestToken);

            LoginBean loginBean = new LoginBean();
            loginBean.setMendeleyLink(oAuthService.getAuthorizationUrl(requestToken));
            request.setAttribute("loginBean", loginBean);

            return "login";
        } else {
            requestToken = (Token) session.getAttribute("requestToken");
            request.removeAttribute("requestToken");

            Profile profile = oAuthService.getProfile(requestToken, authcode);
            session.setAttribute("profile", profile);

            documentDao.createUserFolders(profile.getMain().getProfileId(), mu);

            mu.addMessage(MessageTypes.SUCCESS, "Login successful");

            return "homepage";
        }
    }


}