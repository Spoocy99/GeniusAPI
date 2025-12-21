package dev.spoocy.genius.model;

import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.data.GeniusApiDataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A song is a document hosted on Genius. It’s usually music lyrics.
 * <p>
 * Data for a song includes details about the document itself and
 * information about all the referents that are attached to it, including
 * the text to which they refer.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Song extends GeniusApiDataObject {

    @Nullable
    String get_type();

    @NotNull
    Integer getAnnotationCount();

    @NotNull
    String getApiPath();

    @Nullable
    String getAppleMusicId();

    @Nullable
    String getAppleMusicPlayerUrl();

    @NotNull
    String getArtistNames();

    /**
     * @throws IllegalArgumentException if the provided format is not available
     */
    @NotNull
    String getDescription(@NotNull TextFormat format);

    @Nullable
    String getEmbedContent();

    @NotNull
    String getFullTitle();

    @NotNull
    String getHeaderImageThumbnailUrl();

    @NotNull
    String getHeaderImageUrl();

    @NotNull
    Long getId();

    @Nullable
    String getLanguage();

    @NotNull
    Long getLyricsOwnerId();

    @NotNull
    String getLyricsState();

    @NotNull
    String getPath();

    @NotNull
    String getPrimaryArtistNames();

    @NotNull
    Integer getPyongsCount();

    @Nullable
    String getRecordingLocation();

    @NotNull
    String getRelationshipsIndexUrl();

    @Nullable
    String getReleaseDate();

    @NotNull
    String getReleaseDateForDisplay();

    @NotNull
    String getSongArtImageThumbnailUrl();

    @NotNull
    String getSongArtImageUrl();

    @NotNull
    Stats getStats();

    @NotNull
    String getTitle();

    @NotNull
    String getTitleWithFeatured();

    @NotNull
    String getUrl();

    @Nullable
    CurrentUserMetaData getCurrentUserMetaData();

    @Nullable
    String getSongArtPrimaryColor();

    @Nullable
    String getSongArtSecondaryColor();

    @Nullable
    String getSongArtTextColor();

    @Nullable
    Album getAlbum();

    @Nullable
    List<CustomPerformance> getCustomPerformances();

    @Nullable
    Referent getDescriptionAnnotation();

    @Nullable
    List<Artist> getFeaturedArtists();

    @Nullable
    User getLyricsMarkedCompleteBy();

    @Nullable
    User getLyricsMarkedByStaffApprovedBy();

    @Nullable
    List<Media> getMedia();

    @Nullable
    Artist getPrimaryArtist();

    @Nullable
    List<Artist> getPrimaryArtists();

    @Nullable
    List<Artist> getProducerArtists();

    @Nullable
    List<SongRelationship> getRelationships();

    @Nullable
    List<TranslationSong> getTranslationSongs();

    @Nullable
    List<User> getVerifiedAnnotationsBy();

    @Nullable
    List<VerifiedContributor> getVerifiedContributors();

    @Nullable
    List<User> getVerifiedLyricsBy();

    @Nullable
    List<User> getWriterArtists();


    interface Stats {

        @Nullable
        Integer getAcceptedAnnotations();

        @Nullable
        Integer getContributors();

        @Nullable
        Integer getIqEarners();

        @Nullable
        Integer getTranscribers();

        @Nullable
        Integer getUnreviewedAnnotations();

        @Nullable
        Integer getVerifiedAnnotations();

        @Nullable
        Integer getConcurrents();

        @NotNull
        Boolean isHot();

        @Nullable
        Integer getPageviews();

    }

    interface CustomPerformance {

        @NotNull
        String getLabel();

        @NotNull
        Artist[] getArtists();

    }

    interface Media {

        @Nullable
        String getNativeUri();

        @Nullable
        String getAttribution();

        @NotNull
        String getProvider();

        @Nullable
        Integer getStart();

        @NotNull
        String getType();

        @NotNull
        String getUrl();

    }

    interface SongRelationship {

        @NotNull
        String getRelationshipType();

        @NotNull
        String getType();

        @NotNull
        String getUrl();

        @NotNull
        List<Song> getSongs();

    }

    interface TranslationSong {

        @NotNull
        String getApiPath();

        @NotNull
        Long getId();

        @NotNull
        String getLanguage();

        @NotNull
        String getLyricsState();

        @NotNull
        String getPath();

        @NotNull
        String getTitle();

        @NotNull
        String getUrl();

    }

    interface VerifiedContributor {

        @NotNull
        String[] getContributions();

        @NotNull
        Artist getArtist();

        @NotNull
        User getUsers();

    }

}
