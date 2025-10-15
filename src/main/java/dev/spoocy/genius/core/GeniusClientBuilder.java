package dev.spoocy.genius.core;

import dev.spoocy.genius.GeniusClient;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
public class GeniusClientBuilder {

    private static final String DEFAULT_USER_AGENT = "GeniusAPI-Client/1.0.4";

    private String clientId;
    private String clientSecret;
    private String userAgent;
    private String callbackUrl;
    private AuthType authType;

    /**
     * Set the client ID of your application.
     * Required for both authentication methods.
     *
     * @param clientId The client ID of your application.
     *
     * @return The current GeniusClientBuilder for method chaining.
     */
    @Contract("_ -> this")
    public GeniusClientBuilder setClientId(@NotNull String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * Set the client secret of your application.
     * Required for both authentication methods.
     *
     * @param clientSecret The client secret of your application.
     *
     * @return The current GeniusClientBuilder for method chaining.
     */
    @Contract("_ -> this")
    public GeniusClientBuilder setClientSecret(@NotNull String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    /**
     * Set the callback URL of your application.
     * Required only when using the AUTHORIZATION_CODE authentication method.
     *
     * @param callbackUrl The callback URL of your application.
     *
     * @return The current GeniusClientBuilder for method chaining.
     */
    @Contract("_ -> this")
    public GeniusClientBuilder setCallbackUrl(@NotNull String callbackUrl) {
        this.callbackUrl = callbackUrl;
        return this;
    }

    /**
     * Set the authentication type for the GeniusClient.
     *
     * @param authType The authentication type to use.
     *
     * @return The current GeniusClientBuilder for method chaining.
     */
    @Contract("_ -> this")
    public GeniusClientBuilder setAuthType(@NotNull AuthType authType) {
        this.authType = authType;
        return this;
    }

    /**
     * Set the user agent for HTTP requests.
     * If not set, a default user agent will be used.
     * <p>
     * Genius might block User Agents like "Mozilla/5.0" or similar.
     *
     * @param userAgent The user agent to use for HTTP requests.
     *
     * @return The current GeniusClientBuilder for method chaining.
     */
    @Contract("_ -> this")
    public GeniusClientBuilder setUserAgent(@Nullable String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * Builds and returns a GeniusClient instance based on the provided configuration.
     *
     * @throws IllegalArgumentException if required parameters are missing or invalid.
     *
     * @return A configured GeniusClient instance.
     */
    @Contract ("-> new")
    public GeniusClient build() {

        if(authType == null) {
            throw new IllegalArgumentException("AuthType must be provided in the GeniusClientBuilder.");
        }

        if(this.callbackUrl == null && this.authType == AuthType.AUTHORIZATION_CODE) {
            throw new IllegalArgumentException("Callback URL must be provided when authenticating using an authorization code.");
        }

        if(this.userAgent == null) {
            this.userAgent = DEFAULT_USER_AGENT;
        }

        switch (authType) {
            case CLIENT_ACCESS_TOKEN:
                return new AccessClient(this);
            case AUTHORIZATION_CODE:
                return new AuthClient(this);
        }

        return null;
    }

    public enum AuthType {
        CLIENT_ACCESS_TOKEN,
        AUTHORIZATION_CODE
    }

}
