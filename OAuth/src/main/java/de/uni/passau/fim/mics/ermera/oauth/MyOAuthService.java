package de.uni.passau.fim.mics.ermera.oauth;

import com.mendeley.oapi.schema.Profile;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public interface MyOAuthService {
    /**
     * Retrieve the profile
     *
     * @param requestToken request token (obtained previously)
     * @param authcode     the authcode of the user
     * @return the profile
     */
    Profile getProfile(Token requestToken, String authcode);

    /**
     * Retrieve the request token.
     *
     * @return request token
     */
    public Token getRequestToken();

    /**
     * Returns the URL where you should redirect your users to authenticate
     * your application.
     *
     * @param requestToken the request token you need to authorize
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl(Token requestToken);


    /**
     * Retrieve a dummy Profile for offline usage
     *
     * @return the dummy profile
     */
    public Profile getDummyProfile();
}
