package dev.spoocy.genius.core.data.songs;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.model.Song;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Implementation for {@link Song.SongRelationship}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SongRelationshipObject implements Song.SongRelationship {

    private final String relationshipType;
    private final String type;
    private final String url;
    private final List<Song> songs;

    private SongRelationshipObject(
            @NotNull String relationshipType,
            @NotNull String type,
            @NotNull String url,
            @NotNull List<Song> songs
    ) {
        this.relationshipType = relationshipType;
        this.type = type;
        this.url = url;
        this.songs = songs;
    }

    @Override
    public @NotNull String getRelationshipType() {
        return this.relationshipType;
    }

    @Override
    public @NotNull String getType() {
        return this.type;
    }

    @Override
    public @NotNull String getUrl() {
        return this.url;
    }

    @Override
    public @NotNull List<Song> getSongs() {
        return this.songs;
    }

    public static class Parser extends AbstractModelParser<Song.SongRelationship> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {}

        @Override
        public Song.SongRelationship parse0(@NotNull dev.spoocy.utils.config.Config data) {
            List<Song> songs = SongObject.Parser.INSTANCE.parseList(
                    data.getSectionArray("songs")
            );

            return new SongRelationshipObject(
                    data.getString("relationship_type"),
                    data.getString("type"),
                    data.getString("url"),
                    songs
            );
        }
    }

}
