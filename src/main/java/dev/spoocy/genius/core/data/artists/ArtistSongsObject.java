package dev.spoocy.genius.core.data.artists;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.songs.SongObject;
import dev.spoocy.genius.model.ArtistSongs;
import dev.spoocy.genius.model.Song;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ArtistSongsObject implements ArtistSongs {

    @NotNull
    private final List<Song> songs;

    public ArtistSongsObject(@NotNull List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public @NotNull List<Song> getSongs() {
        return this.songs;
    }

    public static class Parser extends AbstractModelParser<ArtistSongs> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {

        }

        @Override
        protected ArtistSongs parse0(@NotNull Config data) {
            List<Song> songs = SongObject.Parser.INSTANCE.parseList(data.getSectionArray("songs"));
            return new ArtistSongsObject(songs);
        }
    }

}
