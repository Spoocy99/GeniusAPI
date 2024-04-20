package dev.spoocy.genius.core;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import dev.spoocy.genius.core.http.GeniusEndpoint;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AuthClient extends Client {

    private final OAuth20Service service;
    private OAuth2AccessToken accessToken;
    private String authorizationCode;

    public AuthClient(GeniusClientBuilder builder) {
        this.status = ClientStatus.STARTING;
        String CLIENT_ID = builder.getClientId();
        String CLIENT_SECRET = builder.getClientSecret();

        if(CLIENT_ID == null || CLIENT_SECRET == null) {
            this.status = ClientStatus.CLOSED;
            throw new IllegalArgumentException("Client ID and Client Secret must be provided in the GenuisClientBuilder.");
        }

        ServiceBuilder authBuilder = new ServiceBuilder(CLIENT_ID)
                .apiSecret(CLIENT_SECRET);

        if(builder.getCallbackUrl() != null) authBuilder.callback(builder.getCallbackUrl());
        if(builder.getUserAgent() != null) authBuilder.userAgent(builder.getUserAgent());

        this.service = authBuilder.build(new GeniusEndpoint());
        this.status = ClientStatus.WAITING;
    }

    @Override
    public InputStream execute(@NotNull String apiEndpoint) throws IOException, ExecutionException, InterruptedException {
        checkReady(true);

        final OAuthRequest request = new OAuthRequest(Verb.GET, this.API_BASE + apiEndpoint);
        service.signRequest(accessToken, request);

        Response response = service.execute(request);

        return response.getStream();
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
        setAccessToken();
    }

    @Override
    public void close() {
        if(isClosed()) return;

        this.status = ClientStatus.CLOSED;
        try { this.service.close(); } catch (IOException ignored) { }
    }

    private void setAccessToken() {

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
