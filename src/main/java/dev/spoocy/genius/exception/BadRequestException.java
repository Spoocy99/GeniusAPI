package dev.spoocy.genius.exception;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class BadRequestException extends GeniusApiException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(@NotNull String message) {
        super(message);
    }

    public BadRequestException(@NotNull String message, @NotNull Throwable throwable) {
        super(message, throwable);
    }

}
