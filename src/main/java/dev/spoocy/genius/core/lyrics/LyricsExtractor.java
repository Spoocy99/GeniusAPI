package dev.spoocy.genius.core.lyrics;

import dev.spoocy.genius.exception.LyricsParseException;
import dev.spoocy.genius.model.Lyrics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.nodes.Document;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface LyricsExtractor {

    String DEFAULT_LYRICS_CONTAINER = "Lyrics__Container-sc-68a46031-1 ibbPVY";

    String DEFAULT_LYRICS_CONTAINER_HEADER = "LyricsHeader__Container-sc-2ca6447a-1 cgxMBK";

    String DEFAULT_TITLE_CONTAINER = "SongHeader-desktop__HiddenMask-sc-908aafe9-13 NwWzO";

    /**
     * Extracts lyrics from the provided HTML document.
     *
     * @param document The HTML document to extract lyrics from.
     * @return The extracted lyrics as a string.
     */
    Lyrics extract(@NotNull Document document, @Nullable String cachedTitle) throws LyricsParseException;

}
