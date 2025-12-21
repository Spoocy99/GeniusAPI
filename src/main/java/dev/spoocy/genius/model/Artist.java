package dev.spoocy.genius.model;

import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.data.GeniusApiDataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Artist extends GeniusApiDataObject {

    @Nullable
    String[] getAlternateNames();

    @NotNull
    String getApiPath();

    /**
     * @throws IllegalArgumentException if the provided format is not available
     */
    @NotNull
    String getDescription(@NotNull TextFormat format);

    @Nullable
    String getFacebookName();

    @NotNull
    String getHeaderImageUrl();

    @NotNull
    Long getId();

    @NotNull
    String getImageUrl();

    @Nullable
    String getInstagramName();

    @NotNull
    Boolean isMemeVerified();

    @NotNull
    Boolean isVerified();

    @NotNull
    String getName();

    @Nullable
    Boolean isTranslationArtist();

    @Nullable
    String getTwitterName();

    @NotNull
    String getUrl();

    @Nullable
    CurrentUserMetaData getCurrentUserMetaData();

    @Nullable
    Integer getFollowersCount();

    @NotNull
    Long getIq();

}
