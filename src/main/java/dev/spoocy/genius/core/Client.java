package dev.spoocy.genius.core;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.request.LyricsRequestBuilder;
import dev.spoocy.genius.core.request.ArtistRequestBuilder;
import dev.spoocy.genius.core.request.SearchRequestBuilder;
import dev.spoocy.genius.core.request.SongRequestBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class Client implements GeniusClient {

    protected ClientStatus status;

    @Override
    public @Nullable LyricsRequestBuilder lyrics() {
        checkReady(true);
        return new LyricsRequestBuilder(this);
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
