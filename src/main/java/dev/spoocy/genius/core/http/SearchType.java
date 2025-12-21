package dev.spoocy.genius.core.http;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public enum SearchType {
    /**
     * per_page: default 20, max 50
     */
    SONG("song", "/api/search/song"),
    LYRIC("lyric", "/api/search/lyric"),
    ARTIST("artist", "/api/search/artist"),
    ALBUM("album", "/api/search/album"),
    VIDEO("video", "/api/search/video"),
    ARTICLE("article", "/api/search/article"),
    USER("user", "/api/search/user"),

    /**
     * per_page: max 5
     */
    MULTI("multi", "/api/search/multi");

    private final String key;
    private final String apiPath;

    SearchType(@NotNull String key, @NotNull String apiPath) {
        this.key = key;
        this.apiPath = apiPath;
    }

    @NotNull
    public String getKey() {
        return this.key;
    }

    @NotNull
    public String getApiPath() {
        return this.apiPath;
    }

    public static SearchType fromKey(@NotNull String key) {
        for (SearchType type : values()) {
            if (type.getKey().equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No SearchType with key: " + key);
    }

}
