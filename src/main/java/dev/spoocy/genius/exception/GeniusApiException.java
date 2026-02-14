package dev.spoocy.genius.exception;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GeniusApiException extends IOException {

    public GeniusApiException() {
        super();
    }

    public GeniusApiException(@NotNull String message) {
        super(message);
    }

    public GeniusApiException(@NotNull String message, @NotNull Throwable throwable) {
        super(message, throwable);
    }

}
