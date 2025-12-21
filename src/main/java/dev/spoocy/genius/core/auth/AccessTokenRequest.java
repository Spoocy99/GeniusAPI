package dev.spoocy.genius.core.auth;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.utils.common.misc.Args;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AccessTokenRequest extends AbstractAuthorizationRequest<GeniusAccessToken> {

    private final Scope[] scopes;

    protected AccessTokenRequest(@NotNull Builder builder) {
        super(builder);
        this.scopes = Args.notNull(builder.scopes, "scope");
    }

    @Override
    protected String execute() throws GeniusApiException, IOException, ParseException {
        return this.executePOST();
    }

    @Override
    protected GeniusAccessToken executeAndParse() throws GeniusApiException, IOException, ParseException {
        String data = execute();

        if (data == null || data.isEmpty()) {
            throw new GeniusApiException("Empty access token response received from the server.");
        }

        JSONObject jsonObject = new JSONObject(data);

        return GeniusAccessToken.of(
                jsonObject.getString("access_token"),
                "Bearer",
                this.scopes
        );

    }

    public static class Builder
            extends AbstractAuthorizationRequest.Builder<GeniusAccessToken, Builder> {

        protected Scope[] scopes = new Scope[0];

        public Builder(@NotNull String clientId, @NotNull String clientSecret) {
            super(clientId, clientSecret);
            this.host(GeniusClient.DEFAULT_URL_BASE);
            this.port(GeniusClient.DEFAULT_AUTH_PORT);
            this.scheme(GeniusClient.DEFAULT_AUTH_SCHEME);
            this.path("/oauth/token");
            this.queryParameter("grant_type", "authorization_code");
            this.queryParameter("response_type", "code");

        }

        /**
         * The code query parameter from the redirect to your {@code redirect_uri}
         *
         * @param code the authorization code
         *
         * @return Builder instance
         */
        public Builder authorizationCode(final String code) {
            Args.notNullOrEmpty(code, "code");
            return this.queryParameter("code", code);
        }

        /**
         * The redirect URI used in the initial authorization request.
         * <p>
         * <b>This must be the same as the one specified in {@link AuthorizationCodeUriRequest}.</b
         *
         * @param redirectUri the redirect URI
         *
         * @return Builder instance
         *
         * @see AuthorizationCodeUriRequest.Builder#redirectUri(String)
         */
        public Builder redirectUri(final String redirectUri) {
            Args.notNullOrEmpty(redirectUri, "redirect_uri");
            return this.queryParameter("redirect_uri", redirectUri);
        }

        /**
         * The {@link Scope}s of the access token.
         * <p>
         * <b>This must be the same as the one specified in {@link AuthorizationCodeUriRequest}.</b>
         *
         * @param scope  the first scope
         * @param scopes additional scopes
         *
         * @return Builder instance
         */
        public Builder scope(final @NotNull Scope scope, @NotNull Scope... scopes) {
            Args.notNull(scope, "scope");
            int totalLength = 1 + scopes.length;
            Scope[] allScopes = new Scope[totalLength];
            allScopes[0] = scope;
            System.arraycopy(scopes, 0, allScopes, 1, scopes.length);

            this.scopes = allScopes;
            this.queryParameter("scope", Scope.format(allScopes));
            return this;
        }

        /**
         * Builds the {@link AccessTokenRequest} instance.
         *
         * @return The built Request
         */
        @Override
        public AccessTokenRequest build() {
            return new AccessTokenRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }
    }

}
