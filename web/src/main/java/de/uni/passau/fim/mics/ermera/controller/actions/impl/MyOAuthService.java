package de.uni.passau.fim.mics.ermera.controller.actions.impl;

import com.mendeley.oapi.schema.Profile;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public interface MyOAuthService {
    Profile getMyProfile(Token accessToken);

    OAuthService getService();
}
