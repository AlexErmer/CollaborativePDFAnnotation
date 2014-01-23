package de.uni.passau.fim.mics.ermera.controller.actions;

import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class AbstractAction implements Action {
    protected HttpSession session;
    protected MessageUtil mu;
    protected Profile profile;
    protected String userid;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        session = request.getSession();
        mu = (MessageUtil) session.getAttribute(MessageUtil.NAME);
        profile = (Profile) session.getAttribute("profile");
        if (profile != null) {
            userid = profile.getMain().getProfileId();
        }

        return executeConcrete(request, response);
    }

    protected abstract String executeConcrete(HttpServletRequest request, HttpServletResponse response) throws ActionException;
}
