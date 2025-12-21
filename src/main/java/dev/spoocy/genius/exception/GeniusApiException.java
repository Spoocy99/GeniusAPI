package dev.spoocy.genius.exception;

import org.apache.hc.core5.http.HttpException;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GeniusApiException extends HttpException {

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
