package dev.spoocy.genius.model;

import dev.spoocy.genius.core.data.GeniusApiDataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The search capability covers all content hosted on Genius (all songs).
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Search extends GeniusApiDataObject {

    @NotNull
    List<Hit> getHits();

    interface Hit {

        @NotNull
        List<Highlight> getHighlights();

        @NotNull
        String getIndex();

        @NotNull
        String getType();

        @NotNull
        Song getResult();

    }

    interface Highlight {

        @Nullable
        String getProperty();

        @Nullable
        String getValue();

        @Nullable
        Boolean isSnippet();

        List<Range> getRanges();

    }

    interface Range {

        @NotNull
        Integer getStart();

        @NotNull
        Integer getEnd();

    }

}
