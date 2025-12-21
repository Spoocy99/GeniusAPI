package dev.spoocy.genius.core.impl;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.auth.GeniusAccessToken;
import dev.spoocy.genius.core.http.IHttpManager;
import dev.spoocy.utils.common.misc.Args;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GeniusClientImpl extends AbstractClient {

    private final IHttpManager httpManager;
    private final String scheme;
    private final String host;
    private final int port;
    private final String clientId;
    private final String clientSecret;
    private GeniusAccessToken accessToken;

    public GeniusClientImpl(
            @NotNull IHttpManager httpManager,
            @NotNull String scheme,
            @NotNull String host,
            int port,
            @NotNull String clientId,
            @NotNull String clientSecret,
            @Nullable GeniusAccessToken accessToken
    ) {
        this.httpManager = Args.notNull(httpManager, "HTTP Manager");
        this.scheme = Args.notNullOrEmpty(scheme, "Scheme");
        this.host = Args.notNullOrEmpty(host, "Host");
        this.port = Args.positive(port, "Port");
        this.clientId = Args.notNullOrEmpty(clientId, "Client ID");
        this.clientSecret = Args.notNullOrEmpty(clientSecret, "Client Secret");
        this.accessToken = accessToken;
    }

    @Override
    public @NotNull IHttpManager getHttpManager() {
        return this.httpManager;
    }

    @Override
    public @NotNull String getScheme() {
        return this.scheme;
    }

    @Override
    public @NotNull String getHost() {
        return this.host;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public @Nullable String getProxyUrl() {
        return this.httpManager.getProxy().getHostName();
    }

    @Override
    public @Nullable Integer getProxyPort() {
        return this.httpManager.getProxy().getPort();
    }

    @Override
    public @Nullable String getProxyUsername() {
        return this.httpManager.getProxyCredentials().getUserName();
    }

    @Override
    public char[] getProxyPassword() {
        return this.httpManager.getProxyCredentials().getPassword();
    }

    @Override
    public @NotNull String getUserAgent() {
        return this.httpManager.getUserAgent();
    }

    @Override
    public @NotNull String getClientId() {
        return this.clientId;
    }

    @Override
    public @NotNull String getClientSecret() {
        return this.clientSecret;
    }

    @Override
    public @Nullable GeniusAccessToken getAccessToken() {
        return this.accessToken;
    }

    @Override
    public void setAccessToken(@NotNull GeniusAccessToken accessToken) {
        this.accessToken = accessToken;
    }
}
