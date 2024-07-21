package dev.spoocy.genius.data;

import dev.spoocy.common.config.Config;
import dev.spoocy.common.config.SectionArray;
import lombok.Getter;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
public class Song {

    private int annotationCount;
    private String apiPath;
    private String appleMusicId;
    private String appleMusicPlayerUrl;
    private String artistNames;
    private Description description;
    private boolean hasFeaturedVideo;
    private String fullTitle;
    private String imageThumbnailUrl;
    private String imageUrl;
    private long id;
    private String language;
    private long lyricsOwnerId;
    private String lyricsState;
    private String path;
    private int pyongsCount;
    private String relationshipsIndexUrl;
    private String releaseDate;
    private String releaseDateForDisplay;
    private String songArtImageThumbnailUrl;
    private String songArtImageUrl;
    private Stats stats;
    private String title;
    private String titleWithFeatured;
    private String url;

    private Album album;
    private Artist artist;
    private Artist[] producerArtists;
    private Media[] media;

    public Song(Config data) {
        this.annotationCount = data.getInt("annotation_count", 0);
        this.apiPath = data.getString("api_path", "null");
        this.appleMusicId = data.getString("apple_music_id", "null");
       this.appleMusicPlayerUrl = data.getString("apple_music_player_url", "null");
        this.artistNames = data.getString("artist_names", "null");
        this.hasFeaturedVideo = data.getBoolean("featured_video", false);
        this.fullTitle = data.getString("full_title", "null");
        this.imageThumbnailUrl = data.getString("header_image_thumbnail_url", "null");
        this.imageUrl = data.getString("header_image_url", "null");
        this.id = data.getLong("id", 0L);
        this.language = data.getString("language", "null");
        this.lyricsOwnerId = data.getLong("lyrics_owner_id", 0L);
        this.lyricsState = data.getString("lyrics_state", "null");
        this.path = data.getString("path", "null");
        this.pyongsCount = data.getInt("pyongs_count", 0);
        this.relationshipsIndexUrl = data.getString("relationships_index_url", "null");
        this.releaseDate = data.getString("release_date", "null");
        this.releaseDateForDisplay = data.getString("release_date_for_display", "null");
        this.songArtImageThumbnailUrl = data.getString("song_art_image_thumbnail_url", "null");
        this.songArtImageUrl = data.getString("song_art_image_url", "null");
        this.stats = new Stats(data.getSection("stats"));
        this.title = data.getString("title", "null");
        this.titleWithFeatured = data.getString("title_with_featured", "null");
        this.url = data.getString("url", "null");

        this.album = new Album(data.getSection("album"));
        this.artist = new Artist(data.getSection("primary_artist"));
        this.producerArtists = getProducerArtists(data.getSectionArray("producer_artists"));
        this.media = getMedia(data.getSectionArray("media"));

        this.description = getDescription(data.getSection("description"));

    }

    private Artist[] getProducerArtists(SectionArray<? extends Config> array) {
        Artist[] artists = new Artist[array.length()];
        for(int i = 0; i < array.length(); i++) {
            Config data = array.get(i);
            artists[i] = new Artist(data);
        }
        return artists;
    }

    private Media[] getMedia(SectionArray<? extends Config> array) {
        Media[] media = new Media[array.length()];
        for(int i = 0; i < array.length(); i++) {
            Config data = array.get(i);
            media[i] = new Media(data);
        }
        return media;
    }

    private Description getDescription(Config data) {
        return new Description(
                data.getString("plain", ""),
                data.getString("html", "")
        );
    }

    @Override
    public String toString() {
        return "Song{" +
                ", annotationCount=" + annotationCount +
                ", apiPath='" + apiPath + '\'' +
                ", appleMusicId='" + appleMusicId + '\'' +
                ", appleMusicPlayerUrl='" + appleMusicPlayerUrl + '\'' +
                ", artistNames='" + artistNames + '\'' +
                ", hasFeaturedVideo=" + hasFeaturedVideo +
                ", fullTitle='" + fullTitle + '\'' +
                ", imageThumbnailUrl='" + imageThumbnailUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", id=" + id +
                ", language='" + language + '\'' +
                ", lyricsOwnerId=" + lyricsOwnerId +
                ", lyricsState=" + lyricsState +
                ", path='" + path + '\'' +
                ", pyongsCount=" + pyongsCount +
                ", relationshipsIndexUrl='" + relationshipsIndexUrl + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", releaseDateForDisplay='" + releaseDateForDisplay + '\'' +
                ", songArtImageThumbnailUrl='" + songArtImageThumbnailUrl + '\'' +
                ", songArtImageUrl='" + songArtImageUrl + '\'' +
                ", stats=" + stats +
                ", title='" + title + '\'' +
                ", titleWithFeatured='" + titleWithFeatured + '\'' +
                ", url='" + url + '\'' +
                ", album=" + album +
                ", artist=" + artist +
                ", producerArtists=" + producerArtists +
                '}';
    }
}
