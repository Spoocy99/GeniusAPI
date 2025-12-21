package dev.spoocy.genius.exception;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ForbiddenException extends GeniusApiException {

    public ForbiddenException() {
        super();
    }

    public ForbiddenException(@NotNull String message) {
        super(message);
    }

    public ForbiddenException(@NotNull String message, @NotNull Throwable throwable) {
        super(message, throwable);
    }

}
