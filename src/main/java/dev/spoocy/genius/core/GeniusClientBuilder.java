package dev.spoocy.genius.core;

import dev.spoocy.genius.GeniusClient;
import lombok.Getter;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
public class GeniusClientBuilder {

    private String clientId;
    private String clientSecret;
    private String userAgent;
    private String callbackUrl;
    private AuthType authType;

    public GeniusClientBuilder setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public GeniusClientBuilder setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public GeniusClientBuilder setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
        return this;
    }

    public GeniusClientBuilder setAuthType(AuthType authType) {
        this.authType = authType;
        return this;
    }

    public GeniusClientBuilder setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public GeniusClient build() {

        if(authType == null) {
            throw new IllegalArgumentException("AuthType must be provided in the GenuisClientBuilder.");
        }

        if(this.callbackUrl == null && this.authType == AuthType.AUTHORIZATION_CODE) {
            throw new IllegalArgumentException("Callback URL must be provided when authenticating using an authorization code.");
        }

        if(this.userAgent == null) {
            this.userAgent = "Mozilla/5.0";
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
