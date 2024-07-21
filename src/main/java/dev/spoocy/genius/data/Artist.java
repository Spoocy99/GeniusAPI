package dev.spoocy.genius.data;

import dev.spoocy.common.config.Config;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
public class Artist {

    private String[] alternateNames;
    private String apiPath;
    private Description description;
    private String facebookName;
    private int followersCount;
    private String headerImageUrl;
    private long id;
    private String imageUrl;
    private String instagramName;
    private boolean memeVerified;
    private boolean verified;
    private String name;
    private boolean isTranslationArtist;
    private String twitterName;
    private String url;
    private long iq;
    private User user;

    public Artist(Config data) {
        this.alternateNames = data.getStringList("alternate_names").toArray(new String[0]);
        this.apiPath = data.getString("api_path", "null");
        this.description = getDescription(data.getSection("description"));
        this.facebookName = data.getString("facebook_name", "null");
        this.followersCount = data.getInt("followers_count", 0);
        this.headerImageUrl = data.getString("header_image_url", "null");
        this.id = data.getLong("id", 0);
        this.imageUrl = data.getString("image_url", "null");
        this.instagramName = data.getString("instagram_name", "null");
        this.memeVerified = data.getBoolean("is_meme_verified", false);
        this.verified = data.getBoolean("is_verified", false);
        this.name = data.getString("name", "null");
        this.isTranslationArtist = data.getBoolean("translation_artist", false);
        this.twitterName = data.getString("twitter_name", "null");
        this.url = data.getString("url", "null");
        this.iq = data.getLong("iq", 0);
        this.user = new User(data.getSection("user"));

    }

    private Description getDescription(Config data) {
        return new Description(
                data.getString("plain", ""),
                data.getString("html", "")
        );
    }

    @Override
    public String toString() {
        return "Artist{" +
                "alternateNames=" + Arrays.toString(alternateNames) +
                ", apiPath='" + apiPath + '\'' +
                ", description=" + description +
                ", facebookName='" + facebookName + '\'' +
                ", followersCount=" + followersCount +
                ", headerImageUrl='" + headerImageUrl + '\'' +
                ", id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", instagramName='" + instagramName + '\'' +
                ", memeVerified=" + memeVerified +
                ", verified=" + verified +
                ", name='" + name + '\'' +
                ", isTranslationArtist=" + isTranslationArtist +
                ", twitterName='" + twitterName + '\'' +
                ", url='" + url + '\'' +
                ", iq=" + iq +
                ", user=" + user +
                '}';
    }
}
