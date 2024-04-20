package dev.spoocy.genius.core;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.data.Lyrics;
import dev.spoocy.genius.core.request.ArtistRequestBuilder;
import dev.spoocy.genius.core.request.SearchRequestBuilder;
import dev.spoocy.genius.core.request.SongRequestBuilder;
import dev.spoocy.genius.exception.GeniusException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

import java.io.IOException;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class Client implements GeniusClient {

    protected ClientStatus status;

    @Nullable
    public Lyrics getLyrics(@NotNull String url) {
        checkReady(true);

        if(!url.startsWith("https://genius.com/")) {
            throw new IllegalArgumentException("Genius URL for lyrics should look like this: https://genius.com/Sia-chandelier-lyrics");
        }

        try {
            Document document = Jsoup.connect(url).userAgent("Mozilla").get();
            document.select("br").append("\\n");
            document.select("p").prepend("\\n\\n");

            Element element = document.getElementsByClass(LYRICS_CONTAINER).get(0);

            if (!element.hasText()) {
                throw new GeniusException("Could not find Lyrics Container. Check the URL and report this to the developer.");
            }

            String lyrics = Jsoup.clean(element.html(), "", Safelist.none(), new Document.OutputSettings().prettyPrint(false)).replace("\\n", "\n");

            return new Lyrics(lyrics);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SongRequestBuilder searchSong() {
        checkReady(true);
        return new SongRequestBuilder(this);
    }

    @Override
    public ArtistRequestBuilder searchArtist() {
        checkReady(true);
        return new ArtistRequestBuilder(this);
    }

    @Override
    public SearchRequestBuilder search() {
        checkReady(true);
        return new SearchRequestBuilder(this);
    }

    @Override
    public boolean isClosed() {
        return this.status == ClientStatus.CLOSED;
    }

    protected void checkReady(boolean wait) throws IllegalStateException {
        if(this.status == ClientStatus.STARTING) {
            throw new IllegalStateException("This client is currently starting. Please try again later.");
        }
        if(this.status == ClientStatus.CLOSED) {
            throw new IllegalStateException("This client has been closed.");
        }

        if(wait && this.status == ClientStatus.WAITING) {
            throw new IllegalStateException("Authorization code is missing. Please authenticate the client by providing the authorization code.");
        }
    }

}
