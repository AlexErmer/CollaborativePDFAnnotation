package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.controller.Views;
import de.uni.passau.fim.mics.ermera.controller.actions.AbstractAction;
import de.uni.passau.fim.mics.ermera.controller.actions.ActionException;
import de.uni.passau.fim.mics.ermera.dao.DocumentDao;
import de.uni.passau.fim.mics.ermera.dao.DocumentDaoImpl;
import de.uni.passau.fim.mics.ermera.model.LoginBean;
import de.uni.passau.fim.mics.ermera.oauth.MendeleyOAuthServiceImpl;
import de.uni.passau.fim.mics.ermera.oauth.MyOAuthService;
import org.scribe.model.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginAction extends AbstractAction {

    @Override
    public String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        DocumentDao documentDao = new DocumentDaoImpl();

        Token requestToken;
        MyOAuthService oAuthService = new MendeleyOAuthServiceImpl();

        if (PropertyReader.OFFLINELOGIN) {
            Profile profile = oAuthService.getDummyProfile();
            documentDao.createUserFolders(profile.getMain().getProfileId(), mu);
            session.setAttribute("profile", profile);
            mu.addMessage(MessageTypes.SUCCESS, "Dummy Login successful");
            return Views.HOMEPAGE.toString();
        }

        String authcode = request.getParameter("oauth_verifier");
        if (authcode == null) {
            requestToken = oAuthService.getRequestToken();
            session.setAttribute("requestToken", requestToken);

            LoginBean loginBean = new LoginBean();
            loginBean.setMendeleyLink(oAuthService.getAuthorizationUrl(requestToken));
            request.setAttribute("loginBean", loginBean);

            return Views.LOGIN.toString();
        } else {
            requestToken = (Token) session.getAttribute("requestToken");
            request.removeAttribute("requestToken");

            Profile profile = oAuthService.getProfile(requestToken, authcode);
            session.setAttribute("profile", profile);

            documentDao.createUserFolders(profile.getMain().getProfileId(), mu);

            mu.addMessage(MessageTypes.SUCCESS, "Login successful");

            return Views.HOMEPAGE.toString();
        }
    }


}