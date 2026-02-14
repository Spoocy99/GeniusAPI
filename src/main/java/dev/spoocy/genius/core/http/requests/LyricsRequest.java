package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.data.lyrics.LyricsObject;
import dev.spoocy.genius.core.http.GeniusApiRequest;
import dev.spoocy.genius.core.http.internal.Header;
import dev.spoocy.genius.core.lyrics.AdvancedExtractor;
import dev.spoocy.genius.core.lyrics.DirectLyricsExtractor;
import dev.spoocy.genius.core.lyrics.LyricsExtractor;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.genius.exception.LyricsParseException;
import dev.spoocy.genius.model.Lyrics;
import dev.spoocy.genius.model.Song;
import dev.spoocy.utils.common.log.ILogger;
import dev.spoocy.utils.common.text.StringUtils;
import dev.spoocy.utils.config.Config;
import dev.spoocy.utils.config.documents.JsonConfig;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.net.URI;

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

    private Mono<URI> uriProvider() {
        if (!this.allowQuery || StringUtils.isNullOrEmpty(this.query)) {
            return Mono.just(this.getUri());
        }

        return this.getExecutingClient()
                .search()
                .query(this.query)
                .build()
                .map(search -> {
                    if (search.getHits().isEmpty()) {
                        LOGGER.debug("No search results found for query '{}', falling back to provided URL.", this.query);
                        return this.getUri();
                    }

                    Song firstQuery = search.getHits().get(0).getResult();
                    this.cachedTitle = firstQuery.getTitle();
                    String uri = firstQuery.getUrl();

                    if (StringUtils.isNullOrEmpty(uri) || !uri.startsWith("https://genius.com/")) {
                        LOGGER.debug("Lyrics URL not found in search result, falling back to provided URL.");
                        return this.getUri();
                    }

                    return URI.create(uri);
                });
    }

    @Override
    protected @NotNull Mono<String> execute0() {
        return uriProvider()
                .flatMap(lyricsUrl -> this.getHttpManager().get(lyricsUrl, this.getHeaders().toArray(Header[]::new)))
                .flatMap(html -> {
                    try {
                        Document document = Jsoup.parse(html);
                        Lyrics lyrics = extractor.extract(document, this.cachedTitle);

                        JSONObject inner = new JSONObject();
                        inner.put("title", lyrics.getTitle());
                        inner.put("lyrics", lyrics.getPlain());

                        JSONObject jsonResponse = new JSONObject();
                        jsonResponse.put("response", inner);

                        return Mono.just(jsonResponse.toString());
                    } catch (LyricsParseException e) {
                        return Mono.error(e);
                    }
                });
    }

    @Override
    protected @NotNull Lyrics parseResponse(@NotNull String response) throws GeniusApiException {
        Config config = new JsonConfig(response).getSection("response");
        String title = config.getString("title", "Unknown Title");
        String lyricsText = config.getString("lyrics", "Lyrics not found.");
        return new LyricsObject(title, lyricsText);
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
