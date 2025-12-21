package dev.spoocy.genius.core;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public enum Sort {
    TITLE("title"),
    POPULARITY("popularity");

    private final String key;

    Sort(@NotNull String key) {
        this.key = key;
    }

    @NotNull
    public String getKey() {
        return this.key;
    }

}
