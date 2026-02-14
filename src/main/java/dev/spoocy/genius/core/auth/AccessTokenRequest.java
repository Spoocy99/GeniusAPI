package dev.spoocy.genius.core.auth;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.http.internal.ContentType;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.utils.common.misc.Args;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import reactor.core.publisher.Mono;

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
    protected @NotNull Mono<String> execute0() {
        return this.executePOST();
    }

    @Override
    protected @NotNull GeniusAccessToken parseResponse(@NotNull String response) throws GeniusApiException {
        if (response.isEmpty()) {
            throw new GeniusApiException("Empty access token response received from the server.");
        }

        JSONObject jsonObject = new JSONObject(response);

        return GeniusAccessToken.of(
                jsonObject.getString("access_token"),
                "Bearer",
                this.scopes
        );
    }

    public static class Builder
            extends AbstractAuthorizationRequest.Builder<GeniusAccessToken, Builder> {

        protected Scope[] scopes = new Scope[0];

        public Builder(final @NotNull String clientId, final @NotNull String clientSecret) {
            super(clientId, clientSecret);
            //this.contentType(ContentType.APPLICATION_FORM_URLENCODED);
            this.contentType(ContentType.APPLICATION_JSON);
            this.host(GeniusClient.DEFAULT_API_BASE);
            this.port(GeniusClient.DEFAULT_AUTH_PORT);
            this.scheme(GeniusClient.DEFAULT_AUTH_SCHEME);
            this.path("/oauth/token");
            this.grantType("authorization_code");
            this.responseType("code");
        }

        /**
         * The type of the grant. For this request, should be "authorization_code".
         *
         * @param type the grant type
         *
         * @return Builder instance
         */
        public Builder grantType(final @NotNull String type) {
            Args.notNullOrEmpty(type, "grant_type");
            return this.bodyParameter("grant_type", type);
        }

        /**
         * The type of the response. For this request, should be "code".
         *
         * @param responseType the response type
         *
         * @return Builder instance
         */
        public Builder responseType(final @NotNull String responseType) {
            Args.notNullOrEmpty(responseType, "response_type");
            return this.bodyParameter("response_type", responseType);
        }

        /**
         * The code query parameter from the redirect to your {@code redirect_uri}
         *
         * @param code the authorization code
         *
         * @return Builder instance
         */
        public Builder code(final String code) {
            Args.notNullOrEmpty(code, "code");
            return this.bodyParameter("code", code);
        }

        /**
         * @deprecated Use {@link #code(String)} instead.
         */
        @Deprecated
        public Builder authorizationCode(final @NotNull String code) {
            return this.code(code);
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
        public Builder redirectUri(final @NotNull String redirectUri) {
            Args.notNullOrEmpty(redirectUri, "redirect_uri");
            return this.bodyParameter("redirect_uri", redirectUri);
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
        public Builder scope(final @NotNull Scope scope, final @NotNull Scope... scopes) {
            Args.notNull(scope, "scope");
            int totalLength = 1 + scopes.length;
            Scope[] allScopes = new Scope[totalLength];
            allScopes[0] = scope;
            System.arraycopy(scopes, 0, allScopes, 1, scopes.length);

            this.scopes = allScopes;
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
