package dev.spoocy.genius.core.auth;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.http.AbstractRequest;
import dev.spoocy.genius.core.http.IRequest;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.utils.common.misc.Args;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;

/**
 * On the authentication page the user can choose to allow
 * your application to access Genius on their behalf. They’ll
 * be asked to sign in (or, if necessary, create an account) first.
 * <p>
 * Then the user is redirected to {@code https://YOUR_REDIRECT_URI/?code=CODE&state=SOME_STATE_VALUE}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AuthorizationCodeUriRequest extends AbstractRequest<URI> {


    protected AuthorizationCodeUriRequest(@NotNull Builder builder) {
        super(builder);
    }

    @Override
    protected String execute() throws GeniusApiException, IOException, ParseException {
        return this.getUri().toString();
    }

    @Override
    protected URI executeAndParse() throws GeniusApiException, IOException, ParseException {
        return this.getUri();
    }

    public static class Builder
            extends AbstractRequest.Builder<URI, Builder> {


        public Builder() {
            super();

            this.host(GeniusClient.DEFAULT_URL_BASE);
            this.port(GeniusClient.DEFAULT_AUTH_PORT);
            this.scheme(GeniusClient.DEFAULT_AUTH_SCHEME);
            this.path("/oauth/authorize");
            this.queryParameter("response_type", "code");
        }

        /**
         * Specifies the client ID of your application.
         *
         * @param clientId your application’s Client ID,
         *                 as listed on the API Client management page
         *
         * @return Builder instance
         */
        public Builder clientId(final String clientId) {
            Args.notNullOrEmpty(clientId, "client_id");
            return this.queryParameter("client_id", clientId);
        }

        /**
         * Specifies the URI Genius will redirect the user to after they’ve authorized your application.
         * <p>
         * <b>It must be the same as the one set for the API client on the management page.</b
         *
         * @param redirectUri your application’s Redirect URI,
         *                    as listed on the API Client management page
         *
         * @return Builder instance
         */
        public Builder redirectUri(final String redirectUri) {
            Args.notNullOrEmpty(redirectUri, "redirect_uri");
            return this.queryParameter("redirect_uri", redirectUri);
        }

        /**
         * Specifies the scopes of access your application is requesting.
         * <p>
         * Access tokens can only be used for resources that are covered by the
         * scopes provided when they created. These are the available scopes and
         * the endpoints they grant permission for:
         *
         * <table border="1">
         *   <tr>
         *     <td> {@link Scope#ME} </td> <td>
         *         {@code GET /account}
         *     </td>
         *   </tr>
         *   <tr>
         *     <td> {@link Scope#CREATE_ANNOTATIONS} </td> <td>
         *         {@code POST /annotations}
         *     </td>
         *   </tr>
         *   <tr>
         *     <td> {@link Scope#MANAGE_ANNOTATIONS} </td> <td>
         *         {@code POST /annotations} <br>
         *         {@code DELETE /annotations}
         *     </td>
         *   </tr>
         *   <tr>
         *       <td> {@link Scope#VOTE} </td> <td>
         *           {@code PUT /annotations/:id/upvote} <br>
         *           {@code PUT /annotations/:id/downvote} <br>
         *           {@code PUT /annotations/:id/unvote}</td>
         *   </tr>
         * </table>
         *
         *
         * @param scope  first scope to request
         * @param scopes additional scopes to request
         *
         * @return Builder instance
         */
        public Builder scope(final @NotNull Scope scope, @NotNull Scope... scopes) {
            Args.notNull(scope, "scope");

            int totalLength = 1 + scopes.length;
            Scope[] allScopes = new Scope[totalLength];
            allScopes[0] = scope;
            System.arraycopy(scopes, 0, allScopes, 1, scopes.length);

            return this.queryParameter("scope", Scope.format(allScopes));
        }

        /**
         * Specifies a value that will be returned with the code redirect
         * for maintaining arbitrary state through the authorization process.
         * <p>
         * One important use for this value is increased security—by including a unique, difficult to guess
         * value (say, a hash of a user session value), potential attackers can be prevented from sending
         * phony redirects to your app.
         *
         * @param value the state value
         *
         * @return Builder instance
         */
        public Builder state(final String value) {
            Args.notNullOrEmpty(value, "state");
            return this.queryParameter("state", value);
        }

        /**
         * Builds the {@link AuthorizationCodeUriRequest} instance.
         *
         * @return The built instance
         */
        @Override
        public AuthorizationCodeUriRequest build() {
            return new AuthorizationCodeUriRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }


    }


}
