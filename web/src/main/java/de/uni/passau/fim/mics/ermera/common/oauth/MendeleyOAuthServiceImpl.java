package de.uni.passau.fim.mics.ermera.common.oauth;

import com.google.gson.*;
import com.mendeley.oapi.schema.Profile;
import de.uni.passau.fim.mics.ermera.common.PropertyReader;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.MendeleyApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
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
    public Profile getMyProfile(Token accessToken) {
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
    public OAuthService getService() {
        return service;
    }
}
