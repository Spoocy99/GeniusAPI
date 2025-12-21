package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.http.GeniusApiRequest;
import dev.spoocy.genius.core.lyrics.AdvancedExtractor;
import dev.spoocy.genius.core.lyrics.DirectLyricsExtractor;
import dev.spoocy.genius.core.lyrics.LyricsExtractor;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.genius.exception.LyricsParseException;
import dev.spoocy.genius.model.Lyrics;
import dev.spoocy.genius.model.Search;
import dev.spoocy.genius.model.Song;
import dev.spoocy.utils.common.log.ILogger;
import dev.spoocy.utils.common.text.StringUtils;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import reactor.util.annotation.Nullable;

import java.io.IOException;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class LyricsRequest extends GeniusApiRequest<Lyrics> {

    private static final ILogger LOGGER = ILogger.forThisClass();

    private final LyricsExtractor extractor;

    private final boolean allowQuery;

    @Nullable
    private final String query;

    @Nullable
    private String cachedTitle;

    protected LyricsRequest(@NotNull Builder builder) {
        super(builder);
        this.extractor = builder.extractor;
        this.allowQuery = builder.allowQuery;
        this.query = builder.query;
    }

    private String retrieveSongLyricsUrl() throws GeniusApiException, IOException, ParseException {
        if (!this.allowQuery || StringUtils.isNullOrEmpty(this.query)) {
            return this.getUri()
                    .toString();
        }

        Search request = queryURI(this.query).executeAndParse();

        if (request.getHits()
                .isEmpty()) {
            LOGGER.debug("No search results found for query '{}', falling back to provided URL.", this.query);
            return this.getUri()
                    .toString();
        }

        Song firstQuery = request.getHits()
                .get(0)
                .getResult();

        this.cachedTitle = firstQuery.getTitle();
        String uri = firstQuery.getUrl();

        if (StringUtils.isNullOrEmpty(uri) || !uri.startsWith("https://genius.com/")) {
            LOGGER.debug("Lyrics URL not found in search result, falling back to provided URL.");
            return this.getUri()
                    .toString();
        }

        return uri;
    }

    @Override
    protected String execute() throws GeniusApiException, IOException, ParseException {
        Lyrics lyrics = this.executeAndParse();
        return "{\"response\": {\"title\": \"" + lyrics.getTitle() + "\",\"lyrics\": " + lyrics.getPlain() + "}}";
    }

    @Override
    protected Lyrics executeAndParse() throws GeniusApiException, IOException, ParseException {
        Document document;
        String lyricsUrl = retrieveSongLyricsUrl();

        try {
            document = Jsoup.connect(lyricsUrl)
                    .userAgent(this.getHttpManager().getUserAgent())
                    .get();
        } catch (IOException e) {
            throw new LyricsParseException("Failed to fetch lyrics page.", e);
        }

        return extractor.extract(document, this.cachedTitle);
    }

    @NotNull
    private SearchRequest queryURI(@NotNull String query) {
        return this.getExecutingClient()
                .search()
                .query(query)
                .build();
    }

    public static class Builder
            extends GeniusApiRequest.Builder<Lyrics, Builder> {

        private LyricsExtractor extractor = DirectLyricsExtractor.DEFAULT_INSTANCE;
        private boolean allowQuery = true;
        private String query = null;

        public Builder(@NotNull GeniusClient client) {
            super(client);
            this.host(GeniusClient.DEFAULT_URL_BASE);
            this.path("/");
        }

        /**
         * Specifies the lyrics extractor to use.
         *
         * @param extractor the lyrics extractor to use
         *
         * @return Builder instance
         */
        public Builder extractor(@NotNull LyricsExtractor extractor) {
            this.extractor = extractor;
            return this;
        }

        /**
         * Specifies the search query to find the lyrics.
         * <p>
         * {@code query} will be ignored when a {@code url} is provided.
         *
         * @param query the term to search for
         *
         * @return Builder instance
         */
        public Builder query(@Nullable String query) {
            this.query = query;
            return this;
        }

        /**
         * Specifies the URL of the lyrics page.
         * e.g. https://genius.com/Wham-last-christmas-lyrics
         *
         * @param url the URL of the lyrics page
         *
         * @return Builder instance
         */
        public Builder url(@NotNull String url) {
            this.allowQuery = false;
            url = url.replaceFirst("https://genius.com/", "")
                        .replaceFirst("http://genius.com/", "");
            return this.path(url);
        }

        /**
         * Specifies the path of the lyrics page.
         * e.g. https://genius.com/Wham-last-christmas-lyrics -> Wham-last-christmas-lyrics
         *
         * @param path the path of the lyrics page
         *
         * @return Builder instance
         */
        @Override
        public Builder path(@NotNull String path) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }

            return super.path(path);
        }

        /**
         * This will scan the whole site and guess the container names. This does not
         * guarantee successful extraction, but may work. This will result in a slower
         * extraction process.
         * <p>
         * <b>This should only be used if the default extractor completely fails or only
         * retrieves part of the lyrics.</b>
         * <br>
         * e.g. when you receive: <br>
         * - LyricsParseException: "Could not find Lyrics Container." <br>
         * - LyricsParseException: "Lyrics Container does not contain Lyrics."
         *
         * @return Builder instance
         */
        public Builder unknownContainerNames() {
            return extractor(AdvancedExtractor.DEFAULT_INSTANCE);
        }

        /**
         * Builds the {@link SearchRequest} instance.
         *
         * @return The built Request
         */
        @Override
        public LyricsRequest build() {
            return new LyricsRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }


    }

}
