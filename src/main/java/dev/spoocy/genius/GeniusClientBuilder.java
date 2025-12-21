package dev.spoocy.genius;

import dev.spoocy.genius.core.auth.GeniusAccessToken;
import dev.spoocy.genius.core.http.IHttpManager;
import dev.spoocy.genius.core.impl.GeniusClientImpl;
import dev.spoocy.utils.common.misc.Args;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GeniusClientBuilder {

    protected IHttpManager httpManager = GeniusClient.DEFAULT_HTTP_MANAGER;
    protected String scheme = GeniusClient.DEFAULT_REQUEST_SCHEME;
    protected String host = GeniusClient.DEFAULT_API_BASE;
    protected int port = GeniusClient.DEFAULT_REQUEST_PORT;

    protected String clientId = null;
    protected String clientSecret = null;
    protected GeniusAccessToken accessToken = null;

    public GeniusClientBuilder() {

    }

    public GeniusClientBuilder httpManager(@NotNull IHttpManager httpManager) {
        this.httpManager = httpManager;
        return this;
    }

    public GeniusClientBuilder scheme(@NotNull String scheme) {
        this.scheme = scheme;
        return this;
    }

    public GeniusClientBuilder host(@NotNull String host) {
        this.host = host;
        return this;
    }

    public GeniusClientBuilder port(int port) {
        this.port = port;
        return this;
    }

    public GeniusClientBuilder clientId(@NotNull String clientId) {
        this.clientId = clientId;
        return this;
    }

    public GeniusClientBuilder clientSecret(@NotNull String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public GeniusClientBuilder accessToken(@Nullable GeniusAccessToken accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public GeniusClientBuilder accessToken(@NotNull String accessToken) {
        return accessToken(GeniusAccessToken.of(
                Args.notNullOrEmpty(accessToken, "Access Token"),
                "Bearer"
        ));
    }

    protected void validate() {
        Args.notNull(httpManager, "HTTP Manager");
        Args.notNullOrEmpty(scheme, "Scheme");
        Args.notNullOrEmpty(host, "Host");
        Args.positive(port, "Port");
        Args.notNullOrEmpty(clientId, "Client ID");
        Args.notNullOrEmpty(clientSecret, "Client Secret");
    }

    @Contract("-> new")
    public @NotNull GeniusClient build() {
        this.validate();
        return new GeniusClientImpl(
                this.httpManager,
                this.scheme,
                this.host,
                this.port,
                this.clientId,
                this.clientSecret,
                this.accessToken
        );
    }


}
