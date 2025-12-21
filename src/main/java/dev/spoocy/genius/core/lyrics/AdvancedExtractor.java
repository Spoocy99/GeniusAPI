package dev.spoocy.genius.core.lyrics;

import dev.spoocy.genius.core.data.lyrics.LyricsObject;
import dev.spoocy.genius.exception.LyricsParseException;
import dev.spoocy.genius.model.Lyrics;
import dev.spoocy.utils.common.log.ILogger;
import dev.spoocy.utils.common.text.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;

/**
 * Advanced extractor that heuristically searches the document for the element
 * most likely to contain lyrics. This is useful when the class names used on
 * the page are unknown or have changed.
 */
public class AdvancedExtractor implements LyricsExtractor {

    private static final ILogger LOGGER = ILogger.forThisClass();

    public static final AdvancedExtractor DEFAULT_INSTANCE = new AdvancedExtractor();

    // Like "Lyrics__Container-sc-68a46031-1 ibbPVY"
    private static final Pattern LYRICS_CONTAINER = Pattern.compile("Lyrics__Container[-\\w]*", Pattern.CASE_INSENSITIVE);

    // Like "LyricsHeader__Container-sc-6f4ef545-1 cORuWd"
    private static final Pattern LYRICS_HEADER_CONTAINER = Pattern.compile("LyricsHeader__Container[-\\w]*", Pattern.CASE_INSENSITIVE);

    // Like "SongHeader-desktop__Title-sc-7a07005e-9 fZuxOF"
    private static final Pattern TITLE_CONTAINER = Pattern.compile("SongHeader-desktop__Title[-\\w]*", Pattern.CASE_INSENSITIVE);

    // Like "ReferentFragment-desktop__Highlight-sc-31c7eced-1 ihgZDh"
    private static final Pattern LYRICS_PART_SPAN = Pattern.compile("ReferentFragment-desktop__Highlight[-\\w]*", Pattern.CASE_INSENSITIVE);

    private AdvancedExtractor() {}

    @Override
    public Lyrics extract(@NotNull final Document document, @Nullable String cachedTitle) throws LyricsParseException {
        Document copy = document.clone();

        copy.select("br")
                .append("\\n");
        copy.select("p")
                .prepend("\\n\\n");

        // contains <p> with the lyrics text
        Elements lyricsContainers = findLyricsContainers(copy);
        if (lyricsContainers.isEmpty()) {
            throw new LyricsParseException("Could not find lyrics containers in the document.");
        }

        // contains literal text of the lyrics and other span/div elements
        for(Element lyricsContainer : lyricsContainers) {
            lyricsContainer.getAllElements().forEach(element -> {

                if (shouldSanitizeFromLyricsContainer(element)) {
                    element.remove();
                }

            });
        }


        String lyrics
                = Jsoup.clean(
                        lyricsContainers.html(),
                        "",
                        Safelist.none(),
                        new Document.OutputSettings().prettyPrint(false)
                )
                .replace("\\n", "\n");

        String title = !StringUtils.isNullOrEmpty(cachedTitle) ? cachedTitle : findTitle(document);
        return new LyricsObject(title == null ? "null" : title, lyrics);
    }

    @Nullable
    private String findTitle(@NotNull final Document document) {
        for (Element element : document.getAllElements()) {
            for (String className : element.classNames()) {

                if (TITLE_CONTAINER.matcher(className).matches()) {
                    return element.text();
                }

            }
        }
        return null;
    }

    @NotNull
    private Elements findLyricsContainers(@NotNull final Document document) {
        Elements candidates = new Elements();

        for (Element element : document.getAllElements()) {
            if (isPossibleLyricsContainer(element)) {
                LOGGER.trace("Found possible lyrics container: " + element.tagName() + " " + element.classNames());
                candidates.add(element);
            }
        }

        return candidates;
    }

    private boolean shouldSanitizeFromLyricsContainer(@NotNull Element element) {
        //LOGGER.trace("Checking if element should be sanitized: " + element.tagName() + " " + element.classNames());

        if (getScoreForLyricsParagraph(element) < 0.0) {
            LOGGER.trace("Sanitizing element from lyrics container: " + element.tagName() + " " + element.classNames());
            return true;
        }

        return false;
    }

    private boolean isPossibleLyricsContainer(@NotNull Element element) {

        for (String className : element.classNames()) {

            if (LYRICS_CONTAINER.matcher(className).matches()) {
                return true;
            }

        }

        for(Attribute attr : element.attributes()) {
            if (attr.getKey().equalsIgnoreCase("data-lyrics-container")
                    && attr.getValue().equalsIgnoreCase("true")) {
                return true;
            }
        }

        return false;
    }

    private double getScoreForLyricsParagraph(@NotNull Element element) {
        double score = 0.0;

        for (String className : element.classNames()) {
            if (LYRICS_HEADER_CONTAINER.matcher(className).matches()) {
                score -= 10.0;
                break;
            }

            if (LYRICS_PART_SPAN.matcher(className).matches()) {
                score += 10.0;
                break;
            }

        }

        if("p".equalsIgnoreCase(element.tagName())
                || "span".equalsIgnoreCase(element.tagName())) {
            score += 2.0;
        }

        if (element.tagName().equalsIgnoreCase("div")
                || element.tagName().equalsIgnoreCase("section")) {
            score -= 0.5;
        }

        for(Attribute attr : element.attributes()) {
            if (attr.getKey().equalsIgnoreCase("data-exclude-from-selection")
                    && attr.getValue().equalsIgnoreCase("true")) {
                score -= 2.0;
            }
        }

        return score;
    }

}
