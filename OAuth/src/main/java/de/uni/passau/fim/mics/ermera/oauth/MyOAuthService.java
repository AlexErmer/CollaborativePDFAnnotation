package de.uni.passau.fim.mics.ermera.oauth;

import com.mendeley.oapi.schema.Profile;

public interface MyOAuthService {
    /**
     * Retrieve the profile
     *
     * @param authcode     the authcode of the user
     * @return the profile
     */
    Profile getProfile(String authcode);

    /**
     * Returns the URL where you should redirect your users to authenticate
     * your application.
     *
     * @return the URL where you should redirect your users
     */
    String getAuthorizationUrl();

    /**
     * Retrieve a dummy Profile for offline usage
     *
     * @return the dummy profile
     */
    Profile getDummyProfile();
}
