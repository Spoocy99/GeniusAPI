package dev.spoocy.genius.core.request;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.data.Lyrics;
import dev.spoocy.genius.data.Search;
import dev.spoocy.genius.exception.GeniusException;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class LyricsRequestBuilder extends RequestBuilder<Lyrics> {

    private static final String LYRICS_CONTAINER = "Lyrics__Container-sc-cbcfa1dc-1 dfzvqs";
    private static final String LYRICS_CONTAINER_HEADER = "LyricsHeader__Container-sc-5b7f377c-1 bMHdTY";
    private static final String TITLE_CONTAINER = "SongHeader-desktop__HiddenMask-sc-e33a5cab-13 ccUdQo";

    private String url;
    private String query;
    private String cachedTitle;

    public LyricsRequestBuilder(GeniusClient client) {
        super(client);
    }

    @Override
    protected JSONObject getDataObject(JSONObject response) {
        return null;
    }

    @Override
    protected String buildEndpointUrl() {
        if(isNullOrEmpty(url) && isNullOrEmpty(query)) {
            throw new IllegalArgumentException("ID or Name of Song must be set!");
        }

        String endpoint = url != null ? url : getUrl(query);

        if(!endpoint.startsWith("https://genius.com/")) {
            throw new IllegalArgumentException("Genius URL for lyrics should look like this: https://genius.com/Sia-chandelier-lyrics");
        }

        return endpoint;
    }

    @Override
    protected Lyrics buildObject(@NotNull Config data) {
        return null;
    }

    @Override
    public Lyrics execute() throws GeniusException {
        return getLyrics();
    }

    @Override
    public void subscribe(@NotNull CoreSubscriber<? super Lyrics> coreSubscriber) {
        try {
            Mono.just(getLyrics())
                    .doOnError(coreSubscriber::onError)
                    .subscribe(coreSubscriber);
        } catch (Exception e) {
            coreSubscriber.onError(e);
        }
    }

    private Lyrics getLyrics() {
        try {
            Document document = Jsoup
                    .connect(buildEndpointUrl())
                    .userAgent(getClient().getUserAgent())
                    .get();

            document.select("br").append("\\n");
            document.select("p").prepend("\\n\\n");

            Element lyricsElement = document.getElementsByClass(LYRICS_CONTAINER).get(0);

            if (!lyricsElement.hasText()) {
                throw new GeniusException("Could not find Lyrics Container. Check the URL and report this to the developer.");
            }

            // Remove the header "Lyrics" from the lyrics container if it exists
            lyricsElement.getElementsByClass(LYRICS_CONTAINER_HEADER).forEach(Element::remove);

            String lyrics = Jsoup.clean(lyricsElement.html(), "", Safelist.none(), new Document.OutputSettings().prettyPrint(false)).replace("\\n", "\n");
            String title = !isNullOrEmpty(cachedTitle) ? cachedTitle : document.getElementsByClass(TITLE_CONTAINER).get(0).text();

            return new Lyrics(title, lyrics);
        } catch (IOException e) {
            throw new GeniusException("Error while getting Song Lyrics.", e);
        }
    }

    private String getUrl(String query) {
        Search s = getClient().search().setQuery(query).execute();

        if(!s.hasResults()) {
            return "";
        }

        cachedTitle = s.getResults().get(0).getTitle();
        return s.getResults().get(0).getUrl();
    }

    public LyricsRequestBuilder setSongUrl(String url) {
        this.url = url;
        return this;
    }

    public LyricsRequestBuilder setSongName(String query) {
        this.query = query;
        return this;
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}
