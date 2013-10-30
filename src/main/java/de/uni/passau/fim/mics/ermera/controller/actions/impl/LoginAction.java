package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.google.gson.*;
import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.controller.actions.Action;
import de.uni.passau.fim.mics.ermera.model.LoginBean;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.MendeleyApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;

public class LoginAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Token requestToken;
        OAuthService service;

        String authcode = request.getParameter("oauth_verifier");
        if (authcode == null) {
            service = new ServiceBuilder()
                    .provider(MendeleyApi.class)
                    .apiKey(PropertyReader.MENDELEY_API_KEY)
                    .apiSecret(PropertyReader.MENDELEY_SECRET_KEY)
                    .callback(PropertyReader.APPLICATION_URI + "/pages/login")
                    .build();

            // Obtain the Request Token
            requestToken = service.getRequestToken();

            LoginBean loginBean = new LoginBean();
            loginBean.setMendeleyLink(service.getAuthorizationUrl(requestToken));

            request.setAttribute("loginBean", loginBean);
            request.getSession().setAttribute("requestToken", requestToken);
            request.getSession().setAttribute("service", service);

            return "login";
        } else {
            requestToken = (Token) request.getSession().getAttribute("requestToken");
            service = (OAuthService) request.getSession().getAttribute("service");

            Verifier verifier = new Verifier(authcode);

            // Trade the Request Token and Verfier for the Access Token
            Token accessToken = service.getAccessToken(requestToken, verifier);
            request.getSession().setAttribute("accessToken", accessToken);
            request.removeAttribute("requestToken");

            // Now let's go and ask for a protected resource!
            OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://api.mendeley.com/oapi/profiles/info/me");
            service.signRequest(accessToken, oAuthRequest);
            Response oAuthResponse = oAuthRequest.send();

            // parse response to Profile
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(new InputStreamReader(oAuthResponse.getStream()));
            GsonBuilder builder = new GsonBuilder();
            builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
            Gson gson = builder.create();
            Profile profile = gson.fromJson(element, Profile.class);
            request.getSession().setAttribute("profile", profile);

            return "homepage";
        }
    }
}