package dev.spoocy.genius.exception;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99
 * Created on 18.02.2024
 * Discord: spoocy99
 * GitHub: Spoocy99
 */

public class GeniusException extends RuntimeException {

    public GeniusException(@NotNull String message) {
        super(message);
    }

    public GeniusException(@NotNull Throwable throwable) {
        super(throwable);
    }

    public GeniusException(@NotNull String message, @NotNull Throwable throwable) {
        super(message, throwable);
    }

}
