package dev.spoocy.genius.model;

import dev.spoocy.genius.core.data.GeniusApiDataObject;
import dev.spoocy.genius.core.http.SearchType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The search capability covers all content hosted on Genius (all songs).
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Sections extends GeniusApiDataObject {

    @NotNull
    List<Section> getSections();

    @Nullable
    Integer getNextPage();

    interface Section {

        @NotNull
        SearchType getType();

        @NotNull
        List<Hit> getHits();

    }

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

        @NotNull
        String getProperty();

        @NotNull
        String getValue();

        @NotNull
        Boolean isSnippet();

        @NotNull
        List<Range> getRanges();

    }

    interface Range {

        @NotNull
        Integer getStart();

        @NotNull
        Integer getEnd();

    }

}
