package dev.spoocy.genius.core.http;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.auth.GeniusAccessToken;
import dev.spoocy.genius.core.data.GeniusApiDataObject;
import dev.spoocy.utils.common.misc.Args;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class GeniusApiRequest<T> extends AbstractRequest<T> {

    private final GeniusClient client;

    protected GeniusApiRequest(@NotNull Builder<T, ?> builder) {
        super(builder);
        this.client = builder.client;
    }

    protected GeniusClient getExecutingClient() {
        return this.client;
    }

    public static abstract class Builder<T, R extends AbstractRequest.Builder<T, R>>
            extends AbstractRequest.Builder<T, R> {

        protected final GeniusClient client;
        public Builder(@NotNull GeniusClient client) {
            this.client = Args.notNull(client, "client");

            this.header("Authorization", "Bearer " + accessToken(client));
            this.httpManager(client.getHttpManager());
            this.scheme(client.getScheme());
            this.host(client.getHost());
            this.port(client.getPort());
        }

        /**
         * Retrieves the access token for making requests.
         *
         * @return The access token string.
         *
         * @throws IllegalStateException if no access token is set.
         */
        private String accessToken(@NotNull GeniusClient client) {
            GeniusAccessToken GeniusAccessToken = client.getAccessToken();
            if (GeniusAccessToken == null) {
                throw new IllegalStateException("Cannot make requests to the Genius API with no access token set.");
            }
            return GeniusAccessToken.accessToken();
        }

    }

}
