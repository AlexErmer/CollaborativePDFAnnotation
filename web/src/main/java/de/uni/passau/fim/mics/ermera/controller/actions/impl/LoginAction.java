package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import com.mendeley.oapi.schema.User;
import de.uni.passau.fim.mics.ermera.common.MessageTypes;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;
import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.common.oauth.MendeleyOAuthServiceImpl;
import de.uni.passau.fim.mics.ermera.common.oauth.MyOAuthService;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.model.LoginBean;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public class LoginAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MessageUtil mu = (MessageUtil) request.getSession().getAttribute(MessageUtil.NAME);

        if (PropertyReader.OFFLINELOGIN) {
            User user = new User();
            user.setUserId("dummyUser");
            user.setProfileId("dummyUser");
            user.setName("dummyUser");

            Profile profile = new Profile();
            profile.setMain(user);

            createFolders("dummyUser", mu);

            request.getSession().setAttribute("profile", profile);

            mu.addMessage(MessageTypes.SUCCESS, "Login successful");
            return "homepage";
        }

        Token requestToken;
        MyOAuthService oAuthService = new MendeleyOAuthServiceImpl();
        OAuthService service = oAuthService.getService();

        String authcode = request.getParameter("oauth_verifier");
        if (authcode == null) {
            requestToken = service.getRequestToken();
            request.getSession().setAttribute("requestToken", requestToken);

            LoginBean loginBean = new LoginBean();
            loginBean.setMendeleyLink(service.getAuthorizationUrl(requestToken));
            request.setAttribute("loginBean", loginBean);

            return "login";
        } else {
            Verifier verifier = new Verifier(authcode);
            requestToken = (Token) request.getSession().getAttribute("requestToken");

            Token accessToken = service.getAccessToken(requestToken, verifier);
            request.getSession().setAttribute("accessToken", accessToken);
            request.removeAttribute("requestToken");

            Profile profile = oAuthService.getMyProfile(accessToken);
            request.getSession().setAttribute("profile", profile);

            createFolders(profile.getMain().getProfileId(), mu);

            mu.addMessage(MessageTypes.SUCCESS, "Login successful");

            return "homepage";
        }
    }

    private void createFolders(String userid, MessageUtil mu) {
        createFolder(PropertyReader.UPLOAD_PATH, userid, mu);
        createFolder(PropertyReader.STORAGE_PATH, userid, mu);
        createFolder(PropertyReader.BRAT_WORKING_PATH, userid, mu);
        createFolder(PropertyReader.MODEL_PATH, userid, mu);
    }

    private void createFolder(String path, String userid, MessageUtil mu) {
        File dir = new File(PropertyReader.DATA_PATH + userid + "\\" + path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                mu.addMessage(MessageTypes.ERROR, "Directory \"" + path + "\" not created!");
            }
        }
    }
}