package dev.spoocy.genius.core.data.album;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.artists.ArtistObject;
import dev.spoocy.genius.model.Album;
import dev.spoocy.genius.model.Artist;
import dev.spoocy.utils.config.Config;
import dev.spoocy.utils.config.SectionArray;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
    private final Artist artist;

    @NotNull
    private final Artist[] primaryArtists;

    public AlbumObject(
            @NotNull String apiPath,
            @NotNull String coverArtUrl,
            @NotNull String fullTitle,
            @NotNull Long id,
            @NotNull String name,
            @NotNull String primaryArtistName,
            @NotNull String releaseDateForDisplay,
            @NotNull String url,
            @NotNull Artist artist,
            @NotNull Artist[] primaryArtists
    ) {
        this.apiPath = apiPath;
        this.coverArtUrl = coverArtUrl;
        this.fullTitle = fullTitle;
        this.id = id;
        this.name = name;
        this.primaryArtistName = primaryArtistName;
        this.releaseDateForDisplay = releaseDateForDisplay;
        this.url = url;
        this.artist = artist;
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
    public @NotNull Artist getArtist() { return this.artist; }

    @Override
    public @NotNull Artist[] getPrimaryArtists() { return this.primaryArtists; }

    public static final class Parser extends AbstractModelParser<Album> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {}

        @Override
        public Album parse0(@NotNull Config data) {
            SectionArray<? extends Config> primaryArray = data.getSectionArray("primary_artists");
            List<Artist> primaryList = new ArrayList<>();
            for(int i = 0; i < primaryArray.length(); i++) {
                primaryList.add(ArtistObject.Parser.INSTANCE.parse(primaryArray.get(i)));
            }
            Artist[] primaryArtists = primaryList.toArray(new Artist[0]);

            return new AlbumObject(
                    data.getString("api_path", ""),
                    data.getString("cover_art_url", ""),
                    data.getString("full_title", ""),
                    data.getLong("id", 0L),
                    data.getString("name", ""),
                    data.getString("primary_artist_name", ""),
                    data.getString("release_date_for_display", ""),
                    data.getString("url", ""),
                    ArtistObject.Parser.INSTANCE.parse(data.getSection("artist")),
                    primaryArtists
            );
        }
    }

}

