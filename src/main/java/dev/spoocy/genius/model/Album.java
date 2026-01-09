package dev.spoocy.genius.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Album {

    @NotNull
    String getApiPath();

    @NotNull
    String getCoverArtUrl();

    @NotNull
    String getFullTitle();

    @NotNull
    Long getId();

    @NotNull
    String getName();

    @NotNull
    String getPrimaryArtistName();

    @NotNull
    String getReleaseDateForDisplay();

    @NotNull
    String getUrl();

    /**
     * @deprecated Use {@link #getPrimaryArtists()} instead.
     */
    @Nullable
    @Deprecated(since = "1.0.7")
    default Artist getArtist() {
        return getPrimaryArtist();
    }

    /**
     * @deprecated Use {@link #getPrimaryArtists()} instead.
     */
    @Nullable
    @Deprecated(since = "1.0.7")
    default Artist getPrimaryArtist() {
        return !getPrimaryArtists().isEmpty()
                ? getPrimaryArtists().get(0)
                : null;
    }

    @NotNull
    List<Artist> getPrimaryArtists();

}
