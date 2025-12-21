package dev.spoocy.genius.core.lyrics;

import dev.spoocy.genius.core.data.lyrics.LyricsObject;
import dev.spoocy.genius.exception.LyricsParseException;
import dev.spoocy.genius.model.Lyrics;
import dev.spoocy.utils.common.misc.Args;
import dev.spoocy.utils.common.text.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class DirectLyricsExtractor implements LyricsExtractor {

    public static DirectLyricsExtractor DEFAULT_INSTANCE = new Builder().build();

    private final String lyrics_container;
    private final String lyrics_container_header;
    private final String lyrics_title_container;

    public DirectLyricsExtractor(Builder builder) {
        this.lyrics_container = Args.notNullOrEmpty(builder.lyricsContainer, "lyrics_container");
        this.lyrics_container_header = Args.notNullOrEmpty(builder.lyricsContainerHeader, "lyrics_container_header");
        this.lyrics_title_container = Args.notNullOrEmpty(builder.lyricsTitleContainer, "lyrics_title_container");
    }

    @Override
    public Lyrics extract(@NotNull final Document document, @Nullable String cachedTitle) throws LyricsParseException {
        Document copy = document.clone();

        copy.select("br")
                .append("\\n");
        copy.select("p")
                .prepend("\\n\\n");

        // Use the modified clone so the appended/prepended newline placeholders are present
        Elements lyricsElements = copy.getElementsByClass(lyrics_container);

        if (lyricsElements.isEmpty()) {
            throw new LyricsParseException("Could not find Lyrics Container. This happens when the wrong container ids are provided to the DirectLyricsExtractor.");
        }

        for(Element lyricsElement : lyricsElements) {
            lyricsElement.getElementsByClass(lyrics_container_header)
                        .forEach(Element::remove);
        }

        String lyrics = Jsoup.clean(
                        lyricsElements.html(),
                        "",
                        Safelist.none(),
                        new Document.OutputSettings().prettyPrint(false)
                )
                .replace("\\n", "\n");

        // Use the clone for title lookup and guard against missing title element
        Elements titleElements = copy.getElementsByClass(lyrics_title_container);
        String title = !StringUtils.isNullOrEmpty(cachedTitle) ? cachedTitle : (titleElements.isEmpty() ? "" : titleElements.get(0).text());

        return new LyricsObject(
                title,
                lyrics
        );
    }


    public static class Builder {

        private String lyricsContainer = LyricsExtractor.DEFAULT_LYRICS_CONTAINER;
        private String lyricsContainerHeader = LyricsExtractor.DEFAULT_LYRICS_CONTAINER_HEADER;
        private String lyricsTitleContainer = LyricsExtractor.DEFAULT_TITLE_CONTAINER;

        /**
         * Set the class name used to search for lyrics container.
         *
         * @param lyricsContainer the class name of the lyrics container
         */
        public Builder lyrics_container(@NotNull String lyricsContainer) {
            this.lyricsContainer = lyricsContainer;
            return this;
        }

        /**
         * Set the class name used to search for lyrics container header.
         *
         * @param lyricsContainerHeader the class name of the lyrics container header
         */
        public Builder lyrics_container_header(@NotNull String lyricsContainerHeader) {
            this.lyricsContainerHeader = lyricsContainerHeader;
            return this;
        }

        /**
         * Set the class name used to search for lyrics title container.
         *
         * @param lyricsTitleContainer the class name of the lyrics title container
         */
        public Builder lyrics_title_container(@NotNull String lyricsTitleContainer) {
            this.lyricsTitleContainer = lyricsTitleContainer;
            return this;
        }

        public DirectLyricsExtractor build() {
            return new DirectLyricsExtractor(this);
        }

    }

}
