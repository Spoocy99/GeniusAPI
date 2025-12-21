package dev.spoocy.genius.core.data.artists;

import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.account.CurrentUserMetaDataObject;
import dev.spoocy.genius.model.Artist;
import dev.spoocy.genius.model.CurrentUserMetaData;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Map;

/**
 * Implementation for {@link Artist}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ArtistObject implements Artist {

    @Nullable
    private final String[] alternateNames;

    @NotNull
    private final String apiPath;

    @NotNull
    private final Map<TextFormat, String> description;

    @Nullable
    private final String facebookName;

    @NotNull
    private final String headerImageUrl;

    @NotNull
    private final Long id;

    @NotNull
    private final String imageUrl;

    @Nullable
    private final String instagramName;

    @NotNull
    private final Boolean memeVerified;

    @NotNull
    private final Boolean verified;

    @NotNull
    private final String name;

    @Nullable
    private final Boolean translationArtist;

    @Nullable
    private final String twitterName;

    @NotNull
    private final String url;

    @Nullable
    private final CurrentUserMetaData currentUserMetaData;

    @Nullable
    private final Integer followersCount;

    @NotNull
    private final Long iq;

    public ArtistObject(
            @Nullable String[] alternateNames,
            @NotNull String apiPath,
            @NotNull Map<TextFormat, String> description,
            @Nullable String facebookName,
            @NotNull String headerImageUrl,
            @NotNull Long id,
            @NotNull String imageUrl,
            @Nullable String instagramName,
            @NotNull Boolean memeVerified,
            @NotNull Boolean verified,
            @NotNull String name,
            @Nullable Boolean translationArtist,
            @Nullable String twitterName,
            @NotNull String url,
            @Nullable CurrentUserMetaData currentUserMetaData,
            @Nullable Integer followersCount,
            @NotNull Long iq
    ) {
        this.alternateNames = alternateNames;
        this.apiPath = apiPath;
        this.description = description;
        this.facebookName = facebookName;
        this.headerImageUrl = headerImageUrl;
        this.id = id;
        this.imageUrl = imageUrl;
        this.instagramName = instagramName;
        this.memeVerified = memeVerified;
        this.verified = verified;
        this.name = name;
        this.translationArtist = translationArtist;
        this.twitterName = twitterName;
        this.url = url;
        this.currentUserMetaData = currentUserMetaData;
        this.followersCount = followersCount;
        this.iq = iq;
    }

    @Override
    public @Nullable String[] getAlternateNames() {
        return this.alternateNames;
    }

    @Override
    public @NotNull String getApiPath() {
        return this.apiPath;
    }

    @Override
    public @NotNull String getDescription(@NotNull TextFormat format) {
        String d = this.description.get(format);
        if(d == null) {
            throw new IllegalArgumentException("Description does not contain format: " + format.getKey());
        }
        return d;
    }

    @Override
    public @Nullable String getFacebookName() {
        return this.facebookName;
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
    public @NotNull String getImageUrl() {
        return this.imageUrl;
    }

    @Override
    public @Nullable String getInstagramName() {
        return this.instagramName;
    }

    @Override
    public @NotNull Boolean isMemeVerified() {
        return this.memeVerified;
    }

    @Override
    public @NotNull Boolean isVerified() {
        return this.verified;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @Nullable Boolean isTranslationArtist() {
        return this.translationArtist;
    }

    @Override
    public @Nullable String getTwitterName() {
        return this.twitterName;
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
    public @Nullable Integer getFollowersCount() {
        return this.followersCount;
    }

    @Override
    public @NotNull Long getIq() {
        return this.iq;
    }

    public static final class Parser extends AbstractModelParser<Artist> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {}

        @Override
        public Artist parseResponse(String json) {
            return this.parse(
                new JSONObject(json)
                        .getJSONObject("response")
                        .getJSONObject("artist")
        );
        }

        @Override
        public Artist parse0(@NotNull Config data) {
            return new ArtistObject(
                    stringArrayOrNull(data, "alternate_names"),
                    data.getString("api_path", ""),
                    parseFormats(data.getSection("description")),
                    data.getString("facebook_name", ""),
                    data.getString("header_image_url", ""),
                    data.getLong("id", 0L),
                    data.getString("image_url", ""),
                    data.getString("instagram_name", ""),
                    data.getBoolean("is_meme_verified", false),
                    data.getBoolean("is_verified", false),
                    data.getString("name", ""),
                    booleanOrNull(data, "translation_artist"),
                    data.getString("twitter_name", ""),
                    data.getString("url", ""),
                    // current_user_metadata may be absent
                    CurrentUserMetaDataObject.Parser.INSTANCE.parse(data.getSection("current_user_metadata")),
                    data.isSet("followers_count") ? data.getInt("followers_count") : null,
                    data.getLong("iq", 0L)
            );
        }
    }

}
