package dev.spoocy.genius.core.data.songs;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.model.Song;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation for {@link Song.Media}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class MediaObject implements Song.Media {

    /**
     * Path: stats.native_uri
     */
    @Nullable
    private final String nativeUri;

    /**
     * Path: stats.attribution
     */
    @Nullable
    private final String attribution;

    /**
     * Path: stats.provider
     */
    @NotNull
    private final String provider;

    /**
     * Path: stats.start
     */
    @Nullable
    private final Integer start;

    /**
     * Path: stats.type
     */
    @NotNull
    private final String type;

    /**
     * Path: stats.url
     */
    @NotNull
    private final String url;

    public MediaObject(
            @Nullable String nativeUri,
            @Nullable String attribution,
            @NotNull String provider,
            @Nullable Integer start,
            @NotNull String type,
            @NotNull String url
    ) {
        this.nativeUri = nativeUri;
        this.attribution = attribution;
        this.provider = provider;
        this.start = start;
        this.type = type;
        this.url = url;
    }

    @Override
    public @Nullable String getNativeUri() {
        return this.nativeUri;
    }

    @Override
    public @Nullable String getAttribution() {
        return this.attribution;
    }

    @Override
    public @NotNull String getProvider() {
        return this.provider;
    }

    @Override
    public @Nullable Integer getStart() {
        return this.start;
    }

    @Override
    public @NotNull String getType() {
        return this.type;
    }

    @Override
    public @NotNull String getUrl() {
        return this.url;
    }

    public static class Parser extends AbstractModelParser<Song.Media> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        public Song.Media parse0(@NotNull Config data) {
            return new MediaObject(
                    stringOrNull(data, "native_uri"),
                    stringOrNull(data, "attribution"),
                    data.getString("provider", ""),
                    integerOrNull(data, "start"),
                    data.getString("type", ""),
                    data.getString("url", "")
            );
        }
    }

}
