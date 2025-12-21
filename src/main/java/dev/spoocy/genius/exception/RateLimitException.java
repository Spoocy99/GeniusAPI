package dev.spoocy.genius.exception;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class RateLimitException extends GeniusApiException {

    public RateLimitException() {
        super();
    }

    public RateLimitException(@NotNull String message) {
        super(message);
    }

    public RateLimitException(@NotNull String message, @NotNull Throwable throwable) {
        super(message, throwable);
    }
}
