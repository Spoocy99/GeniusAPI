package dev.spoocy.genius.core.data.songs;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.artists.ArtistObject;
import dev.spoocy.genius.model.Artist;
import dev.spoocy.genius.model.Song;
import dev.spoocy.utils.config.Config;
import dev.spoocy.utils.config.SectionArray;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation for {@link Song.CustomPerformance}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class CustomPerformanceObject implements Song.CustomPerformance {

    /**
     * Path: custom_performances.label
     */
    @NotNull
    private final String label;

    /**
     * Path: custom_performances.artists
     */
    @NotNull
    private final Artist[] artists;

    public CustomPerformanceObject(@NotNull String label, @NotNull Artist[] artists) {
        this.label = label;
        this.artists = artists;
    }


    @Override
    public @NotNull String getLabel() {
        return this.label;
    }

    @Override
    public @NotNull Artist[] getArtists() {
        return this.artists;
    }

    public static class Parser extends AbstractModelParser<Song.CustomPerformance> {
        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        public Song.CustomPerformance parse0(@NotNull Config data) {
            SectionArray<? extends Config> artistsData = data.getSectionArray("artists");

            return new CustomPerformanceObject(
                    data.getString("label", ""),
                    ArtistObject.Parser.INSTANCE.parseList(artistsData).toArray(Artist[]::new)
            );
        }
    }

}
