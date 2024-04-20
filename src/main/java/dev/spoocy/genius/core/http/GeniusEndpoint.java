package dev.spoocy.genius.core.http;

import com.github.scribejava.core.builder.api.DefaultApi20;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GeniusEndpoint extends DefaultApi20 {

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.genius.com/oauth/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://api.genius.com/oauth/authorize";
    }

}
