package dev.spoocy.genius.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Lyrics {

    @NotNull
    String getTitle();

    @NotNull
    String getPlain();

    @NotNull
    List<Part> getParts();

    interface Part {

        @NotNull
        String getTitle();

        @NotNull
        String getText();

    }

}
