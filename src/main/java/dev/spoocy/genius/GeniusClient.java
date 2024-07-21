package dev.spoocy.genius;

import dev.spoocy.genius.core.GeniusClientBuilder;
import dev.spoocy.genius.core.request.LyricsRequestBuilder;
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
     * Get the Client ID that is used to authenticate to the API.
     * <p>
     * This was provided in the {@link GeniusClientBuilder} when building the client.
     * @see GeniusClientBuilder#setClientId(String)
     *
     * @return The Client ID.
     */
    String getClientId();

    /**
     * Get the Client Secret that is used to authenticate to the API.
     * <p>
     * This was provided in the {@link GeniusClientBuilder} when building the client.
     * @see GeniusClientBuilder#setClientSecret(String)
     *
     * @return The Client Secret.
     */
    String getClientSecret();

    /**
     * Get the url the client will redirect to after the user has authenticated.
     * <p>
     * Only needed when using {@link GeniusClientBuilder.AuthType#AUTHORIZATION_CODE}
     * to authenticate.
     * <p>
     * This was provided in the {@link GeniusClientBuilder} when building the client.
     * @see GeniusClientBuilder#setCallbackUrl(String)
     *
     * @return The Client ID.
     */
    String getCallbackUrl();

    /**
     * Get the user agent that is used in the header when making requests to the API.
     * <p>
     * This was provided in the {@link GeniusClientBuilder} when building the client.
     * @see GeniusClientBuilder#setUserAgent(String)
     *
     * @return The User Agent.
     */
    String getUserAgent();

    /**
     * Searches for the {@link Lyrics} of a song.
     *
     * @return A {@link LyricsRequestBuilder} to build the request.
     */
    LyricsRequestBuilder lyrics();

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
     * to authenticate.
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

}
