package dev.spoocy.genius.core.data.album;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.artists.ArtistObject;
import dev.spoocy.genius.model.Album;
import dev.spoocy.genius.model.Artist;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Implementation for {@link Album}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AlbumObject implements Album {

    @NotNull
    private final String apiPath;

    @NotNull
    private final String coverArtUrl;

    @NotNull
    private final String fullTitle;

    @NotNull
    private final Long id;

    @NotNull
    private final String name;

    @NotNull
    private final String primaryArtistName;

    @NotNull
    private final String releaseDateForDisplay;

    @NotNull
    private final String url;

    @NotNull
    private final List<Artist> primaryArtists;

    public AlbumObject(
            @NotNull String apiPath,
            @NotNull String coverArtUrl,
            @NotNull String fullTitle,
            @NotNull Long id,
            @NotNull String name,
            @NotNull String primaryArtistName,
            @NotNull String releaseDateForDisplay,
            @NotNull String url,
            @NotNull List<Artist> primaryArtists
    ) {
        this.apiPath = apiPath;
        this.coverArtUrl = coverArtUrl;
        this.fullTitle = fullTitle;
        this.id = id;
        this.name = name;
        this.primaryArtistName = primaryArtistName;
        this.releaseDateForDisplay = releaseDateForDisplay;
        this.url = url;
        this.primaryArtists = primaryArtists;
    }

    @Override
    public @NotNull String getApiPath() { return this.apiPath; }

    @Override
    public @NotNull String getCoverArtUrl() { return this.coverArtUrl; }

    @Override
    public @NotNull String getFullTitle() { return this.fullTitle; }

    @Override
    public @NotNull Long getId() { return this.id; }

    @Override
    public @NotNull String getName() { return this.name; }

    @Override
    public @NotNull String getPrimaryArtistName() { return this.primaryArtistName; }

    @Override
    public @NotNull String getReleaseDateForDisplay() { return this.releaseDateForDisplay; }

    @Override
    public @NotNull String getUrl() { return this.url; }

    @Override
    public @NotNull List<Artist> getPrimaryArtists() {
        return this.primaryArtists;
    }

    public static final class Parser extends AbstractModelParser<Album> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {}

        @Override
        public Album parse0(@NotNull Config data) {
            return new AlbumObject(
                    data.getString("api_path", ""),
                    data.getString("cover_art_url", ""),
                    data.getString("full_title", ""),
                    data.getLong("id", 0L),
                    data.getString("name", ""),
                    data.getString("primary_artist_name", ""),
                    data.getString("release_date_for_display", ""),
                    data.getString("url", ""),
                    ArtistObject.Parser.INSTANCE.parseList(data.getSectionArray("primary_artists"))
            );
        }
    }

}

