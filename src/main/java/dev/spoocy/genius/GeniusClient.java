package dev.spoocy.genius;

import dev.spoocy.genius.core.GeniusClientBuilder;
import dev.spoocy.genius.data.Artist;
import dev.spoocy.genius.data.Lyrics;
import dev.spoocy.genius.data.Search;
import dev.spoocy.genius.data.Song;
import dev.spoocy.genius.core.request.ArtistRequestBuilder;
import dev.spoocy.genius.core.request.SearchRequestBuilder;
import dev.spoocy.genius.core.request.SongRequestBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface GeniusClient extends Closeable {

    /**
     * Get the {@link Lyrics} of a song from a Genius URL.
     * <p>
     * URL should look like this: {@code https://genius.com/Sia-chandelier-lyrics} <br>
     * URLs like this might not work: {@code https://genius.com/songs/378195}
     *
     * @param url The URL of the song.
     *
     * @return The lyrics of the song.
     */
    @Nullable
    Lyrics getLyrics(@NotNull String url);

    /**
     * Searches for a {@link Song}.
     *
     * @return A {@link SongRequestBuilder} to build the request.
     */
    SongRequestBuilder searchSong();

    /**
     * Searches for a {@link Artist}.
     *
     * @return A {@link ArtistRequestBuilder} to build the request.
     */
    ArtistRequestBuilder searchArtist();

    /**
     * Requests a {@link Search}.
     *
     * @return A {@link SearchRequestBuilder} to build the request.
     */
    SearchRequestBuilder search();

    /**
     * Builds the Authorization URL.
     * <p>
     * Only needed when using {@link GeniusClientBuilder.AuthType#AUTHORIZATION_CODE}
     * when building the {@link GeniusClient}.
     *
     * @see GeniusClientBuilder#setAuthType(GeniusClientBuilder.AuthType)
     *
     * @param secretState The secret state.
     *                    A value that will be returned with the code redirect for maintaining
     *                    arbitrary state through the authorization process
     *
     * @return The URL.
     */
    String buildAuthorizationUrl(@NotNull String secretState);

    /**
     * Sets the Authorization Code that is used to authenticate to the Genius API.
     * <p>
     * <strong> When using {@link GeniusClientBuilder.AuthType#AUTHORIZATION_CODE} as Authentication Methode: </strong> <br>
     * This should be the code that is returned when the user authenticates using the Authorization URL.
     * <p>
     * <strong> When using {@link GeniusClientBuilder.AuthType#CLIENT_ACCESS_TOKEN} as Authentication Methode: </strong> <br>
     * This should be the access token found when creating your clients at {@code https://genius.com/api-clients}.
     *
     * @see GeniusClientBuilder#setAuthType(GeniusClientBuilder.AuthType) Set the authentication type.
     * @param code The Authorization Code.
     */
    void setAuthorizationCode(@NotNull String code);

    /**
     * Checks if the client is closed. If it is, no requests can be made.
     *
     * @see #close() Close the client.
     *
     * @return {@code true} if the client is closed, {@code false} otherwise.
     */
    boolean isClosed();

    /**
     * Closes the client. No requests can be made after the client is closed.
     */
    void close();

    @Nullable
    InputStream execute(@NotNull String apiEndpoint)  throws IOException, ExecutionException, InterruptedException;

    String API_BASE = "https://api.genius.com/";
    String GENIUS_BASE = "https://genius.com/";
    String LYRICS_CONTAINER = "Lyrics__Container-sc-1ynbvzw-1 kUgSbL";

}
