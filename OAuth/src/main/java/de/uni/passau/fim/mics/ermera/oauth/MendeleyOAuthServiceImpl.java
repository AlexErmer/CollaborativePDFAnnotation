package de.uni.passau.fim.mics.ermera.oauth;

import com.google.gson.*;
import com.mendeley.oapi.schema.Profile;
import com.mendeley.oapi.schema.User;
import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.MendeleyApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.io.InputStreamReader;


public class MendeleyOAuthServiceImpl implements MyOAuthService {
    protected OAuthService service;

    public MendeleyOAuthServiceImpl() {
        service = new ServiceBuilder()
                .provider(MendeleyApi.class)
                .apiKey(PropertyReader.MENDELEY_API_KEY)
                .apiSecret(PropertyReader.MENDELEY_SECRET_KEY)
                .callback(PropertyReader.APPLICATION_URI + "/pages/login")
                .build();
    }

    @Override
    public Profile getProfile(Token requestToken, String authcode) {
        Verifier verifier = new Verifier(authcode);
        Token accessToken = service.getAccessToken(requestToken, verifier);

        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, "http://api.mendeley.com/oapi/profiles/info/me");
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
    public Profile getDummyProfile() {
        User user = new User();
        user.setUserId("dummyUser");
        user.setProfileId("dummyUser");
        user.setName("dummyUser");

        Profile profile = new Profile();
        profile.setMain(user);
        return profile;
    }
}
