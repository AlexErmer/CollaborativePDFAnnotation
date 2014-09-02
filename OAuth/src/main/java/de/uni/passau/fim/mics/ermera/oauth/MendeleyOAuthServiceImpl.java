package de.uni.passau.fim.mics.ermera.oauth;

import com.google.gson.*;
import com.mendeley.oapi.schema.Profile;
import com.mendeley.oapi.schema.User;
import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.MendeleyApi20;
import org.scribe.extractors.TokenExtractor20Impl;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.io.InputStreamReader;


public class MendeleyOAuthServiceImpl implements MyOAuthService {
    protected OAuthService service;

    public MendeleyOAuthServiceImpl() {
        service = new ServiceBuilder()
                .provider(MendeleyApi20.class)
                .apiKey(PropertyReader.MENDELEY_API_KEY)
                .apiSecret(PropertyReader.MENDELEY_SECRET_KEY)
                .scope("all")
                .callback(PropertyReader.APPLICATION_URI + "/pages/login")
                .build();
    }

    @Override
    public Profile getProfile(Token requestToken, String authcode) {
        Verifier verifier = new Verifier(authcode);
        Token accessToken = service.getAccessToken(null, verifier);

/*        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api-oauth2.mendeley.com/oauth/token");
        request.addBodyParameter(OAuthConstants.CLIENT_ID, PropertyReader.MENDELEY_API_KEY);
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, PropertyReader.MENDELEY_SECRET_KEY);
        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI, "http://localhost:8080/pages/login");
        request.addBodyParameter("grant_type", "authorization_code");
        Response response = request.send();
        Token extract = new TokenExtractor20Impl().extract(response.getBody());*/

        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "https://api-oauth2.mendeley.com/oapi/profiles/info/me");
        service.signRequest(accessToken, oAuthRequest);
        Response oAuthResponse = oAuthRequest.send();

        // parse response to Profile
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(new InputStreamReader(oAuthResponse.getStream()));
        GsonBuilder builder = new GsonBuilder();
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = builder.create();
        return gson.fromJson(element, Profile.class);
    }

    @Override
    public Token getRequestToken() {
        return service.getRequestToken();
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return service.getAuthorizationUrl(requestToken);
    }

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        return service.getAccessToken(requestToken, verifier);
    }

    @Override
    public Profile getDummyProfile() {
        final String dummyUserName = "dummyUser3";

        User user = new User();
        user.setUserId(dummyUserName);
        user.setProfileId(dummyUserName);
        user.setName(dummyUserName);

        Profile profile = new Profile();
        profile.setMain(user);
        return profile;
    }
}
