package dev.spoocy.genius.core.auth;

import dev.spoocy.genius.core.http.AbstractRequest;
import dev.spoocy.utils.common.misc.Args;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractAuthorizationRequest<T> extends AbstractRequest<T> {

    protected AbstractAuthorizationRequest(@NotNull Builder<T, ?> builder) {
        super(builder);
    }


    public static abstract class Builder<T, R extends AbstractRequest.Builder<T, R>>
            extends AbstractRequest.Builder<T, R> {

        protected final String clientId;
        protected final String clientSecret;

        public Builder(@NotNull String clientId, @NotNull String clientSecret) {
            this.clientId = Args.notNullOrEmpty(clientId, "clientId");
            this.clientSecret = Args.notNullOrEmpty(clientSecret, "clientSecret");

            this.queryParameter("client_id", clientId);
            this.queryParameter("client_secret", clientSecret);
        }

    }

}
