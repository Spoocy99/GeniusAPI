package dev.spoocy.genius.exception;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class LyricsParseException extends GeniusApiException {

    public LyricsParseException() {
        super();
    }

    public LyricsParseException(@NotNull String message) {
        super(message);
    }

    public LyricsParseException(@NotNull String message, @NotNull Throwable throwable) {
        super(message, throwable);
    }
}
