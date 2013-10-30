package de.uni.passau.fim.mics.ermera.controller.actions;

import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import de.uni.passau.fim.mics.ermera.model.LoginBean;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.MendeleyApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().removeAttribute("accessToken");
        request.getSession().removeAttribute("service");
        request.getSession().removeAttribute("profile");

        return "login";
    }
}