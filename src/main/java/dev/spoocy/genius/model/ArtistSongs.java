package dev.spoocy.genius.model;

import dev.spoocy.genius.core.data.GeniusApiDataObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ArtistSongs extends GeniusApiDataObject {

    @NotNull
    List<Song> getSongs();

}
