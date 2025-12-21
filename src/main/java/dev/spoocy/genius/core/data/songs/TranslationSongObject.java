package dev.spoocy.genius.core.data.songs;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.model.Song;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation for {@link Song.TranslationSong}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class TranslationSongObject implements Song.TranslationSong {

    private final String apiPath;
    private final Long id;
    private final String language;
    private final String lyricsState;
    private final String path;
    private final String title;
    private final String url;

    private TranslationSongObject(
            @NotNull String apiPath,
            @NotNull Long id,
            @NotNull String language,
            @NotNull String lyricsState,
            @NotNull String path,
            @NotNull String title,
            @NotNull String url
    ) {
        this.apiPath = apiPath;
        this.id = id;
        this.language = language;
        this.lyricsState = lyricsState;
        this.path = path;
        this.title = title;
        this.url = url;
    }

    @Override
    public @NotNull String getApiPath() {
        return this.apiPath;
    }

    @Override
    public @NotNull Long getId() {
        return this.id;
    }

    @Override
    public @NotNull String getLanguage() {
        return this.language;
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
    public @NotNull String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull String getUrl() {
        return this.url;
    }


    public static class Parser extends AbstractModelParser<Song.TranslationSong> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        public @NotNull Song.TranslationSong parse0(@NotNull Config data) {
            return new TranslationSongObject(
                    data.getString("api_path", ""),
                    data.getLong("id", 0L),
                    data.getString("language", ""),
                    data.getString("lyrics_state", ""),
                    data.getString("path", ""),
                    data.getString("title", ""),
                    data.getString("url", "")
            );
        }

    }
}
