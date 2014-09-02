package org.scribe.oauth;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;

/**
 * extends {@code OAuth20ServiceImpl} in order to be able to use mendeley oauth2 by using bodyparameters instead of querystring parameters.
 */
public class MendeleyOAuth20ServiceImpl extends OAuth20ServiceImpl {
    private final DefaultApi20 api;
    private final OAuthConfig config;

    /**
     * Default constructor
     *
     * @param api    OAuth2.0 api information
     * @param config OAuth 2.0 configuration param object
     */
    public MendeleyOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
        this.api = api;
        this.config = config;
    }

    /**
     * does the same as the anchestor, but uses bodyparameters!
     */
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        request.addBodyParameter("grant_type", "authorization_code");
        if (config.hasScope()) {
            request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
        }
        Response response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }
}
