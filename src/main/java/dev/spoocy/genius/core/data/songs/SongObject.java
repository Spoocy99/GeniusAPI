package dev.spoocy.genius.core.data.songs;

import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.account.CurrentUserMetaDataObject;
import dev.spoocy.genius.core.data.account.UserObject;
import dev.spoocy.genius.core.data.album.AlbumObject;
import dev.spoocy.genius.core.data.artists.ArtistObject;
import dev.spoocy.genius.core.data.referents.ReferentObject;
import dev.spoocy.genius.model.*;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Implementation for {@link Song}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SongObject implements Song {

    @Nullable
    private final String _type;

    @NotNull
    private final Integer annotationCount;

    @NotNull
    private final String apiPath;

    @Nullable
    private final String appleMusicId;

    @Nullable
    private final String appleMusicPlayerUrl;

    @NotNull
    private final String artistNames;

    @NotNull
    private final Map<TextFormat, String> description;

    @Nullable
    private final String embedContent;

    @NotNull
    private final String fullTitle;

    @NotNull
    private final String headerImageThumbnailUrl;

    @NotNull
    private final String headerImageUrl;

    @NotNull
    private final Long id;

    @Nullable
    private final String language;

    @NotNull
    private final Long lyricsOwnerId;

    @NotNull
    private final String lyricsState;

    @NotNull
    private final String path;

    @NotNull
    private final String primaryArtistNames;

    @NotNull
    private final Integer pyongsCount;

    @Nullable
    private final String recordingLocation;

    @NotNull
    private final String relationshipsIndexUrl;

    @Nullable
    private final String releaseDate;

    @NotNull
    private final String releaseDateForDisplay;

    @NotNull
    private final String songArtImageThumbnailUrl;

    @NotNull
    private final String songArtImageUrl;

    @NotNull
    private final Song.Stats stats;

    @NotNull
    private final String title;

    @NotNull
    private final String titleWithFeatured;

    @NotNull
    private final String url;

    @Nullable
    private final CurrentUserMetaData currentUserMetaData;

    @Nullable
    private final String songArtPrimaryColor;

    @Nullable
    private final String songArtSecondaryColor;

    @Nullable
    private final String songArtTextColor;

    @Nullable
    private final Album album;

    @Nullable
    private final List<Song.CustomPerformance> customPerformances;

    @Nullable
    private final Referent descriptionAnnotation;

    @Nullable
    private final List<Artist> featuredArtists;

    @Nullable
    private final User lyricsMarkedCompleteBy;

    @Nullable
    private final User lyricsMarkedByStaffApprovedBy;

    @Nullable
    private final List<Media> media;

    @Nullable
    private final Artist primaryArtist;

    @Nullable
    private final List<Artist> primaryArtists;

    @Nullable
    private final List<Artist> producerArtists;

    @Nullable
    private final List<Song.SongRelationship> relationships;

    @Nullable
    private final List<Song.TranslationSong> translationSongs;

    @Nullable
    private final List<User> verifiedAnnotationsBy;

    @Nullable
    private final List<VerifiedContributor> verifiedContributors;

    @Nullable
    private final List<User> verifiedLyricsBy;

    @Nullable
    private final List<User> writerArtists;

    public SongObject(
            @Nullable String _type,
            @NotNull Integer annotationCount,
            @NotNull String apiPath,
            @Nullable String appleMusicId,
            @Nullable String appleMusicPlayerUrl,
            @NotNull String artistNames,
            @NotNull Map<TextFormat, String> description,
            @Nullable String embedContent,
            @NotNull String fullTitle,
            @NotNull String headerImageThumbnailUrl,
            @NotNull String headerImageUrl,
            @NotNull Long id,
            @Nullable String language,
            @NotNull Long lyricsOwnerId,
            @NotNull String lyricsState,
            @NotNull String path,
            @NotNull String primaryArtistNames,
            @NotNull Integer pyongsCount,
            @Nullable String recordingLocation,
            @NotNull String relationshipsIndexUrl,
            @Nullable String releaseDate,
            @NotNull String releaseDateForDisplay,
            @NotNull String songArtImageThumbnailUrl,
            @NotNull String songArtImageUrl,
            @NotNull Song.Stats stats,
            @NotNull String title,
            @NotNull String titleWithFeatured,
            @NotNull String url,
            @Nullable CurrentUserMetaData currentUserMetaData,
            @Nullable String songArtPrimaryColor,
            @Nullable String songArtSecondaryColor,
            @Nullable String songArtTextColor,
            @Nullable Album album,
            @Nullable List<Song.CustomPerformance> customPerformances,
            @Nullable Referent descriptionAnnotation,
            @Nullable List<Artist> featuredArtists,
            @Nullable User lyricsMarkedCompleteBy,
            @Nullable User lyricsMarkedByStaffApprovedBy,
            @Nullable List<Media> media,
            @Nullable Artist primaryArtist,
            @Nullable List<Artist> primaryArtists,
            @Nullable List<Artist> producerArtists,
            @Nullable List<Song.SongRelationship> relationships,
            @Nullable List<Song.TranslationSong> translationSongs,
            @Nullable List<User> verifiedAnnotationsBy,
            @Nullable List<VerifiedContributor> verifiedContributors,
            @Nullable List<User> verifiedLyricsBy,
            @Nullable List<User> writerArtists
    ) {
        this._type = _type;
        this.annotationCount = annotationCount;
        this.apiPath = apiPath;
        this.appleMusicId = appleMusicId;
        this.appleMusicPlayerUrl = appleMusicPlayerUrl;
        this.artistNames = artistNames;
        this.description = description;
        this.embedContent = embedContent;
        this.fullTitle = fullTitle;
        this.headerImageThumbnailUrl = headerImageThumbnailUrl;
        this.headerImageUrl = headerImageUrl;
        this.id = id;
        this.language = language;
        this.lyricsOwnerId = lyricsOwnerId;
        this.lyricsState = lyricsState;
        this.path = path;
        this.primaryArtistNames = primaryArtistNames;
        this.pyongsCount = pyongsCount;
        this.recordingLocation = recordingLocation;
        this.relationshipsIndexUrl = relationshipsIndexUrl;
        this.releaseDate = releaseDate;
        this.releaseDateForDisplay = releaseDateForDisplay;
        this.songArtImageThumbnailUrl = songArtImageThumbnailUrl;
        this.songArtImageUrl = songArtImageUrl;
        this.stats = stats;
        this.title = title;
        this.titleWithFeatured = titleWithFeatured;
        this.url = url;
        this.currentUserMetaData = currentUserMetaData;
        this.songArtPrimaryColor = songArtPrimaryColor;
        this.songArtSecondaryColor = songArtSecondaryColor;
        this.songArtTextColor = songArtTextColor;
        this.album = album;
        this.customPerformances = customPerformances;
        this.descriptionAnnotation = descriptionAnnotation;
        this.featuredArtists = featuredArtists;
        this.lyricsMarkedCompleteBy = lyricsMarkedCompleteBy;
        this.lyricsMarkedByStaffApprovedBy = lyricsMarkedByStaffApprovedBy;
        this.media = media;
        this.primaryArtist = primaryArtist;
        this.primaryArtists = primaryArtists;
        this.producerArtists = producerArtists;
        this.relationships = relationships;
        this.translationSongs = translationSongs;
        this.verifiedAnnotationsBy = verifiedAnnotationsBy;
        this.verifiedContributors = verifiedContributors;
        this.verifiedLyricsBy = verifiedLyricsBy;
        this.writerArtists = writerArtists;
    }

    @Override
    public @Nullable String get_type() {
        return this._type;
    }

    @Override
    public @NotNull Integer getAnnotationCount() {
        return this.annotationCount;
    }

    @Override
    public @NotNull String getApiPath() {
        return this.apiPath;
    }

    @Override
    public @Nullable String getAppleMusicId() {
        return this.appleMusicId;
    }

    @Override
    public @Nullable String getAppleMusicPlayerUrl() {
        return this.appleMusicPlayerUrl;
    }

    @Override
    public @NotNull String getArtistNames() {
        return this.artistNames;
    }

    @Override
    public @NotNull String getDescription(@NotNull TextFormat format) {
        String d = this.description.get(format);
        if (d == null) throw new IllegalArgumentException("Description does not contain format: " + format.getKey());
        return d;
    }

    @Override
    public @Nullable String getEmbedContent() {
        return this.embedContent;
    }

    @Override
    public @NotNull String getFullTitle() {
        return this.fullTitle;
    }

    @Override
    public @NotNull String getHeaderImageThumbnailUrl() {
        return this.headerImageThumbnailUrl;
    }

    @Override
    public @NotNull String getHeaderImageUrl() {
        return this.headerImageUrl;
    }

    @Override
    public @NotNull Long getId() {
        return this.id;
    }

    @Override
    public @Nullable String getLanguage() {
        return this.language;
    }

    @Override
    public @NotNull Long getLyricsOwnerId() {
        return this.lyricsOwnerId;
    }

    @Override
    public @NotNull String getLyricsState() {
        return this.lyricsState;
    }

    @Override
    public @NotNull String getPath() {
        return this.path;
    }

    @Override
    public @NotNull String getPrimaryArtistNames() {
        return this.primaryArtistNames;
    }

    @Override
    public @NotNull Integer getPyongsCount() {
        return this.pyongsCount;
    }

    @Override
    public @Nullable String getRecordingLocation() {
        return this.recordingLocation;
    }

    @Override
    public @NotNull String getRelationshipsIndexUrl() {
        return this.relationshipsIndexUrl;
    }

    @Override
    public @Nullable String getReleaseDate() {
        return this.releaseDate;
    }

    @Override
    public @NotNull String getReleaseDateForDisplay() {
        return this.releaseDateForDisplay;
    }

    @Override
    public @NotNull String getSongArtImageThumbnailUrl() {
        return this.songArtImageThumbnailUrl;
    }

    @Override
    public @NotNull String getSongArtImageUrl() {
        return this.songArtImageUrl;
    }

    @Override
    public @NotNull Song.Stats getStats() {
        return this.stats;
    }

    @Override
    public @NotNull String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull String getTitleWithFeatured() {
        return this.titleWithFeatured;
    }

    @Override
    public @NotNull String getUrl() {
        return this.url;
    }

    @Override
    public @Nullable CurrentUserMetaData getCurrentUserMetaData() {
        return this.currentUserMetaData;
    }

    @Override
    public @Nullable String getSongArtPrimaryColor() {
        return this.songArtPrimaryColor;
    }

    @Override
    public @Nullable String getSongArtSecondaryColor() {
        return this.songArtSecondaryColor;
    }

    @Override
    public @Nullable String getSongArtTextColor() {
        return this.songArtTextColor;
    }

    @Override
    public @Nullable Album getAlbum() {
        return this.album;
    }

    @Override
    public @Nullable List<Song.CustomPerformance> getCustomPerformances() {
        return this.customPerformances;
    }

    @Override
    public @Nullable Referent getDescriptionAnnotation() {
        return this.descriptionAnnotation;
    }

    @Override
    public @Nullable List<Artist> getFeaturedArtists() {
        return this.featuredArtists;
    }

    @Override
    public @Nullable User getLyricsMarkedCompleteBy() {
        return this.lyricsMarkedCompleteBy;
    }

    @Override
    public @Nullable User getLyricsMarkedByStaffApprovedBy() {
        return this.lyricsMarkedByStaffApprovedBy;
    }

    @Override
    public @Nullable List<Media> getMedia() {
        return this.media;
    }

    @Override
    public @Nullable Artist getPrimaryArtist() {
        return this.primaryArtist;
    }

    @Override
    public @Nullable List<Artist> getPrimaryArtists() {
        return this.primaryArtists;
    }

    @Override
    public @Nullable List<Artist> getProducerArtists() {
        return this.producerArtists;
    }

    @Override
    public @Nullable List<Song.SongRelationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public @Nullable List<Song.TranslationSong> getTranslationSongs() {
        return this.translationSongs;
    }

    @Override
    public @Nullable List<User> getVerifiedAnnotationsBy() {
        return this.verifiedAnnotationsBy;
    }

    @Override
    public @Nullable List<VerifiedContributor> getVerifiedContributors() {
        return this.verifiedContributors;
    }

    @Override
    public @Nullable List<User> getVerifiedLyricsBy() {
        return this.verifiedLyricsBy;
    }

    @Override
    public @Nullable List<User> getWriterArtists() {
        return this.writerArtists;
    }

    public static final class Parser extends AbstractModelParser<Song> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        public Song parseResponse(String json) {
            return this.parse(
                new JSONObject(json)
                        .getJSONObject("response")
                        .getJSONObject("song")
        );
        }

        @Override
        public Song parse0(@NotNull final Config data) {

            Album album = null;
            if (data.isSet("album")) {
                album = AlbumObject.Parser.INSTANCE
                        .parse(data.getSection("album"));
            }

            List<Song.CustomPerformance> customPerformances = null;
            if (data.isSet("custom_performances")) {
                customPerformances = CustomPerformanceObject.Parser.INSTANCE
                        .parseList(data.getSectionArray("custom_performances"));
            }

            Referent descAnn = null;
            if (data.isSet("description_annotation")) {
                descAnn = ReferentObject.Parser.INSTANCE
                        .parse(data.getSection("description_annotation"));
            }

            List<Artist> featured = null;
            if (data.isSet("featured_artists")) {
                featured = ArtistObject.Parser.INSTANCE
                        .parseList(data.getSectionArray("featured_artists")
                );
            }

            User lyricsCompleteBy = null;
            if (data.isSet("lyrics_marked_complete_by")) {
                lyricsCompleteBy = UserObject.Parser.INSTANCE
                        .parse(data.getSection("lyrics_marked_complete_by"));
            }

            User staffApprovedBy = null;
            if (data.isSet("lyrics_marked_by_staff_approved_by")) {
                staffApprovedBy = UserObject.Parser.INSTANCE
                        .parse(data.getSection("lyrics_marked_by_staff_approved_by"));
            }

            Artist primaryArtist = null;
            if (data.isSet("primary_artist")) {
                primaryArtist = ArtistObject.Parser.INSTANCE
                        .parse(data.getSection("primary_artist"));
            }

            List<Artist> primaryArtists = null;
            if (data.isSet("primary_artists")) {
                primaryArtists = ArtistObject.Parser.INSTANCE
                        .parseList(data.getSectionArray("primary_artists")
                );
            }

            List<Artist> producers = null;
            if (data.isSet("producer_artists")) {
                producers = ArtistObject.Parser.INSTANCE
                        .parseList(data.getSectionArray("producer_artists")
                );
            }

            CurrentUserMetaData currentUserMetaData = null;
            if (data.isSet("current_user_metadata")) {
                currentUserMetaData = CurrentUserMetaDataObject.Parser.INSTANCE
                        .parse(data.getSection("current_user_metadata"));
            }

            List<SongRelationship> relationships = null;
            if (data.isSet("relationships")) {
                relationships = SongRelationshipObject.Parser.INSTANCE
                        .parseList(data.getSectionArray("song_relationships"));
            }

            List<Song.TranslationSong> translationSongs = null;
            if (data.isSet("translation_songs")) {
                translationSongs = TranslationSongObject.Parser.INSTANCE
                        .parseList(data.getSectionArray("translation_songs"));
            }

            List<User> verifiedAnnotationsBy = null;
            if (data.isSet("verified_annotations_by")) {
                verifiedAnnotationsBy = UserObject.Parser.INSTANCE
                        .parseList(data.getSectionArray("verified_annotations_by"));
            }

            List<VerifiedContributor> verifiedContributors = null;
            if (data.isSet("verified_contributors")) {
                verifiedContributors = VerifiedContributorObject.Parser.INSTANCE
                        .parseList(data.getSectionArray("verified_contributors"));
            }

            List<User> verifiedLyricsBy = null;
            if (data.isSet("verified_lyrics_by")) {
                verifiedLyricsBy = UserObject.Parser.INSTANCE
                        .parseList(data.getSectionArray("verified_lyrics_by"));
            }

            List<User> writerArtists = null;
            if (data.isSet("writer_artists")) {
                writerArtists = UserObject.Parser.INSTANCE
                        .parseList(data.getSectionArray("writer_artists"));
            }

            return new SongObject(
                    stringOrNull(data, "_type"),
                    data.getInt("annotation_count", 0),
                    data.getString("api_path", ""),
                    stringOrNull(data, "apple_music_id"),
                    stringOrNull(data, "apple_music_player_url"),
                    data.getString("artist_names", ""),
                    parseFormats(data.getSection("description")),
                    stringOrNull(data, "embed_content"),
                    data.getString("full_title", ""),
                    data.getString("header_image_thumbnail_url", ""),
                    data.getString("header_image_url", ""),
                    data.getLong("id", 0L),
                    stringOrNull(data, "language"),
                    data.getLong("lyrics_owner_id", 0L),
                    data.getString("lyrics_state", ""),
                    data.getString("path", ""),
                    data.getString("primary_artist_names", ""),
                    data.getInt("pyongs_count", 0),
                    stringOrNull(data, "recording_location"),
                    data.getString("relationships_index_url", ""),
                    stringOrNull(data, "release_date"),
                    data.getString("release_date_for_display", ""),
                    data.getString("song_art_image_thumbnail_url", ""),
                    data.getString("song_art_image_url", ""),
                    StatsObject.Parser.INSTANCE.parse(data.getSection("stats")),
                    data.getString("title", ""),
                    data.getString("title_with_featured", ""),
                    data.getString("url", ""),
                    currentUserMetaData,
                    stringOrNull(data, "song_art_primary_color"),
                    stringOrNull(data, "song_art_secondary_color"),
                    stringOrNull(data, "song_art_text_color"),
                    album,
                    customPerformances,
                    descAnn,
                    featured,
                    lyricsCompleteBy,
                    staffApprovedBy,
                    MediaObject.Parser.INSTANCE.parseList(data.getSectionArray("media")),
                    primaryArtist,
                    primaryArtists,
                    producers,
                    relationships,
                    translationSongs,
                    verifiedAnnotationsBy,
                    verifiedContributors,
                    verifiedLyricsBy,
                    writerArtists
            );
        }
    }

}

