package de.uni.passau.fim.mics.ermera.oauth;

import com.mendeley.oapi.schema.Profile;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

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
    Token getRequestToken();

    /**
     * Returns the URL where you should redirect your users to authenticate
     * your application.
     *
     * @param requestToken the request token you need to authorize
     * @return the URL where you should redirect your users
     */
    String getAuthorizationUrl(Token requestToken);


    Token getAccessToken(Token requestToken, Verifier verifier);

    /**
     * Retrieve a dummy Profile for offline usage
     *
     * @return the dummy profile
     */
    Profile getDummyProfile();
}
