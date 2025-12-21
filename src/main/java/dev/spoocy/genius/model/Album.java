package dev.spoocy.genius.model;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    Artist getArtist();

    @NotNull
    Artist[] getPrimaryArtists();

}
