package dev.spoocy.genius.core;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.http.GeniusEndpoint;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AuthClient extends Client {

    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String USER_AGENT;
    private final String CALLBACK_URI;

    private final OAuth20Service service;
    private OAuth2AccessToken accessToken;
    private String authorizationCode;

    public AuthClient(@NotNull GeniusClientBuilder builder) {
        this.status = ClientStatus.STARTING;
        CLIENT_ID = builder.getClientId();
        CLIENT_SECRET = builder.getClientSecret();
        USER_AGENT = builder.getUserAgent();
        CALLBACK_URI = builder.getCallbackUrl();

        if(CLIENT_ID == null || CLIENT_SECRET == null) {
            this.status = ClientStatus.CLOSED;
            throw new IllegalArgumentException("Client ID and Client Secret must be provided in the GenuisClientBuilder.");
        }

        ServiceBuilder authBuilder = new ServiceBuilder(CLIENT_ID)
                .apiSecret(CLIENT_SECRET);

        authBuilder.callback(CALLBACK_URI);
        authBuilder.userAgent(USER_AGENT);

        this.service = authBuilder.build(new GeniusEndpoint());
        this.status = ClientStatus.WAITING;
    }

    @Override
    public InputStream execute(@NotNull String apiEndpoint) throws IOException, ExecutionException, InterruptedException {
        checkReady(true);

        String uri = GeniusClient.API_BASE + apiEndpoint;

        final OAuthRequest request = new OAuthRequest(Verb.GET, uri);
        service.signRequest(accessToken, request);

        Response response = service.execute(request);

        return response.getStream();
    }

    @Override
    public String getClientId() {
        return this.CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return this.CLIENT_SECRET;
    }

    @Override
    public String getCallbackUrl() {
        return this.CALLBACK_URI;
    }

    @Override
    public String getUserAgent() {
        return this.USER_AGENT;
    }

    @Override
    public String buildAuthorizationUrl(@NotNull String secretState) {
        checkReady(false);
        return this.service.getAuthorizationUrl(secretState);
    }

    @Override
    public void setAuthorizationCode(@NotNull String code) {
        checkReady(false);

        this.authorizationCode = code;
        updateAccessToken();
    }

    @Override
    public void close() {
        if(isClosed()) return;

        this.status = ClientStatus.CLOSED;
        try { this.service.close(); } catch (IOException ignored) { }
    }

    private void updateAccessToken() {

        if(this.authorizationCode == null) {
            throw new IllegalArgumentException("Authorization code is missing. Please authenticate the client by providing the authorization code.");
        }

        try {
            this.accessToken = this.service.getAccessToken(authorizationCode);
            this.status = ClientStatus.READY;
        } catch (IOException | InterruptedException | ExecutionException e) {
            this.status = ClientStatus.WAITING;
            throw new IllegalStateException("Failed to build the OAuth2Service. Please check your credentials and try again.", e);
        }
    }

}
