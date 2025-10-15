package dev.spoocy.genius.data;

import dev.spoocy.utils.config.Config;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
public class SearchSong {

    private int annotationCount;
    private String apiPath;
    private String artistNames;
    private String fullTitle;
    private String imageThumbnailUrl;
    private String imageUrl;
    private long id;
    private long lyricsOwnerId;
    private String lyricsState;
    private String path;
    private String relationshipsIndexUrl;
    private String releaseDateForDisplay;
    private String songArtImageThumbnailUrl;
    private String songArtImageUrl;
    private String title;
    private String titleWithFeatured;
    private String url;

    private Artist artist;

    public SearchSong(@NotNull Config data) {
        this.annotationCount = data.getInt("annotation_count", 0);
        this.apiPath = data.getString("api_path", "null");
        this.artistNames = data.getString("artist_names", "null");
        this.fullTitle = data.getString("full_title", "null");
        this.imageThumbnailUrl = data.getString("header_image_thumbnail_url", "null");
        this.imageUrl = data.getString("header_image_url", "null");
        this.id = data.getLong("id", 0L);
        this.lyricsOwnerId = data.getLong("lyrics_owner_id", 0L);
        this.lyricsState = data.getString("lyrics_state", "null");
        this.path = data.getString("path", "null");
        this.relationshipsIndexUrl = data.getString("relationships_index_url", "null");
        this.releaseDateForDisplay = data.getString("release_date_for_display", "null");
        this.songArtImageThumbnailUrl = data.getString("song_art_image_thumbnail_url", "null");
        this.songArtImageUrl = data.getString("song_art_image_url", "null");
        this.title = data.getString("title", "null");
        this.titleWithFeatured = data.getString("title_with_featured", "null");
        this.url = data.getString("url", "null");
        this.artist = new Artist(data.getSection("primary_artist"));
    }

    @Override
    public String toString() {
        return "SearchSong{" +
                "annotationCount=" + annotationCount +
                ", apiPath='" + apiPath + '\'' +
                ", artistNames='" + artistNames + '\'' +
                ", fullTitle='" + fullTitle + '\'' +
                ", imageThumbnailUrl='" + imageThumbnailUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", id=" + id +
                ", lyricsOwnerId=" + lyricsOwnerId +
                ", lyricsState='" + lyricsState + '\'' +
                ", path='" + path + '\'' +
                ", relationshipsIndexUrl='" + relationshipsIndexUrl + '\'' +
                ", releaseDateForDisplay='" + releaseDateForDisplay + '\'' +
                ", songArtImageThumbnailUrl='" + songArtImageThumbnailUrl + '\'' +
                ", songArtImageUrl='" + songArtImageUrl + '\'' +
                ", title='" + title + '\'' +
                ", titleWithFeatured='" + titleWithFeatured + '\'' +
                ", url='" + url + '\'' +
                ", artist=" + artist +
                '}';
    }
}
