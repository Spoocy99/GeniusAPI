package dev.spoocy.genius.core.auth;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface GeniusAccessToken {

    /**
     * Get the access token string.
     *
     * @return The access token string.
     */
    @NotNull
    String accessToken();

    /**
     * Get the type of the token, typically "Bearer".
     *
     * @return The type of the token.
     */
    @NotNull
    String tokenType();

    /**
     * Get the {@link Scope}s associated with the access token.
     *
     * @return The scopes.
     */
    @NotNull
    Scope[] scopes();

    /**
     * Create a new instance of {@link GeniusAccessToken}.
     *
     * @param accessToken The access token string.
     * @param tokenType   The type of the token.
     * @param scope       The scope associated with the access token.
     *
     * @return A new instance of {@link GeniusAccessToken}.
     */
    static GeniusAccessToken of(
            @NotNull String accessToken,
            @NotNull String tokenType,
            @NotNull Scope... scopes
    ) {
        return new Literal(accessToken, tokenType, scopes);
    }

    class Literal implements GeniusAccessToken {

        private final String accessToken;
        private final String tokenType;
        private final Scope[] scopes;

        public Literal(
                @NotNull String accessToken,
                @NotNull String tokenType,
                @NotNull Scope... scopes
        ) {
            this.accessToken = accessToken;
            this.tokenType = tokenType;
            this.scopes = scopes;
        }

        @Override
        public @NotNull String accessToken() {
            return accessToken;
        }

        @Override
        public @NotNull String tokenType() {
            return tokenType;
        }

        @Override
        public @NotNull Scope[] scopes() {
            return scopes;
        }


    }

}
