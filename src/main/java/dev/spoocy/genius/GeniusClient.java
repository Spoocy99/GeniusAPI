package dev.spoocy.genius;

import dev.spoocy.genius.core.Genius;
import dev.spoocy.genius.core.Sort;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.auth.AccessTokenRequest;
import dev.spoocy.genius.core.auth.AuthorizationCodeUriRequest;
import dev.spoocy.genius.core.auth.GeniusAccessToken;
import dev.spoocy.genius.core.auth.Scope;
import dev.spoocy.genius.core.http.GeniusHttpManager;
import dev.spoocy.genius.core.http.IHttpManager;
import dev.spoocy.genius.core.http.requests.*;
import dev.spoocy.genius.core.http.u_requests.SectionsSearchRequest;
import dev.spoocy.genius.model.Referents;
import dev.spoocy.utils.common.log.ILogger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The main client interface for interacting with the Genius API.
 * <p>
 * This interface provides methods to create requests for various
 * endpoints of the Genius API, such as retrieving annotations,
 * songs, artists, referents, and user account information.
 * It also includes methods for handling authentication
 * via access tokens and authorization codes.
 * <p>
 * The client can be configured using the {@link GeniusClientBuilder}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 * @see GeniusClient#builder()
 */

public interface GeniusClient {

    ILogger LOGGER = ILogger.forClass(GeniusClient.class);

    /*
     * The default base URL for the Genius website.
     */ String DEFAULT_URL_BASE = "genius.com";

    /*
     * The default base URL for the Genius API.
     */ String DEFAULT_API_BASE = "api.genius.com";

    /*
     * The default auth port used for requests.
     */ int DEFAULT_AUTH_PORT = 443;

    /*
     * The default authentication scheme used for requests.
     */ String DEFAULT_AUTH_SCHEME = "https";

    /*
     * The default http manager used for requests.
     */ IHttpManager DEFAULT_HTTP_MANAGER = new GeniusHttpManager.Builder().build();

    /*
     * The default scheme used for requests.
     */ String DEFAULT_REQUEST_SCHEME = "https";

    /*
     * The default port used for requests.
     */ int DEFAULT_REQUEST_PORT = 443;

    /*
     * The default user agent used for requests.
     */ String DEFAULT_USER_AGENT = "GeniusAPI-Client/1.0";


    /**
     * Get the {@link IHttpManager} used for API calls.
     *
     * @return The HTTP Manager.
     */
    @NotNull IHttpManager getHttpManager();

    /**
     * Get the scheme used for API calls.
     *
     * @return The scheme.
     */
    @NotNull String getScheme();

    /**
     * Get the host used for API calls.
     *
     * @return The host.
     */
    @NotNull String getHost();

    /**
     * Get the port used for API calls.
     *
     * @return The port.
     */
    int getPort();

    /**
     * Get the proxy URL if one is set.
     *
     * @return The proxy URL.
     */
    @Nullable String getProxyUrl();

    /**
     * Get the proxy port if one is set.
     *
     * @return The proxy port.
     */
    @Nullable Integer getProxyPort();

    /**
     * Get the proxy username if one is set.
     *
     * @return The proxy username.
     */
    @Nullable String getProxyUsername();

    /**
     * Get the proxy password if one is set.
     *
     * @return The proxy password.
     */
    char[] getProxyPassword();

    /**
     * Get the User Agent used for API calls.
     *
     * @return The User Agent.
     */
    @NotNull String getUserAgent();

    /**
     * Get the Client ID used to authenticate to the API.
     *
     * @return The Client ID.
     */
    @NotNull
    String getClientId();

    /**
     * Get the Client Secret used to authenticate to the API.
     *
     * @return The Client Secret.
     */
    @NotNull
    String getClientSecret();

    /**
     * Gets the Access Token used to authenticate to the Genius API.
     *
     * @return The Access Token or {@code null} if not set.
     */
    @Nullable
    GeniusAccessToken getAccessToken();

    /**
     * Sets the Access Token used to authenticate to the Genius API.
     * <p>
     * <b> This is required for making authenticated requests. </b> <p>
     * <p>
     * There are two types of access tokens:
     * <br>
     * <b> 1) Client Access Token: </b> <br>
     * If your application doesn't include user-specific behaviors you can use the
     * client access token associated with your API instead of tokens for authenticated
     * users. These tokens are only valid for read-only endpoints that are not restricted by a
     * required {@link dev.spoocy.genius.core.auth.Scope}.
     * <p>
     *
     * <b> 2) User Access Token: </b> <br>
     * If your application includes user-specific behaviors you will need to obtain a user
     * access token via the OAuth2 authorization code flow. These tokens are required for
     * endpoints that are restricted by a required {@link dev.spoocy.genius.core.auth.Scope}.
     * <p>
     * <p>
     * Refer to the official Genius API documentation for more information on
     * authentication and access tokens: <a href="https://docs.genius.com/#authentication">Genius API Authentication</a>
     *
     * @param accessToken The Access Token.
     */
    void setAccessToken(@NotNull GeniusAccessToken accessToken);

    /**
     * Creates a new {@link AuthorizationCodeUriRequest.Builder} for getting an access token.
     * <p>
     * This request requires the following parameters to be set before building:
     * <ul>
     *     <li>client_id</li>
     *     <li>redirect_uri</li>
     *     <li>scope</li>
     * </ul>
     * <p>
     * <b>This is required when making requests on behalf of a user. </b>
     *
     * @return The Authorization Code URI Request.
     */
    @Contract("-> new")
    @NotNull
    AuthorizationCodeUriRequest.Builder authorizationCodeUriRequest();

    /**
     * Creates a new {@link AuthorizationCodeUriRequest} for getting an access token
     * and builds it.
     * <p>
     * <b>This is required when making requests on behalf of a user. </b>
     * <p>
     * <b>State:</b>
     * Specifies a value that will be returned with the code redirect
     * for maintaining arbitrary state through the authorization process.
     * <br>
     * One important use for this value is increased security—by including a unique, difficult to guess
     * value (say, a hash of a user session value), potential attackers can be prevented from sending
     * phony redirects to your app.
     * <p>
     * <b>Scopes:</b>
     * Access tokens can only be used for resources that are covered by the
     * scopes provided when they created. These are the available scopes and
     * the endpoints they grant permission for:
     *
     * <table border="1">
     *   <tr>
     *     <td> {@link Scope#ME} </td> <td>
     *         {@code GET /account}
     *     </td>
     *   </tr>
     *   <tr>
     *     <td> {@link Scope#CREATE_ANNOTATIONS} </td> <td>
     *         {@code POST /annotations}
     *     </td>
     *   </tr>
     *   <tr>
     *     <td> {@link Scope#MANAGE_ANNOTATIONS} </td> <td>
     *         {@code POST /annotations} <br>
     *         {@code DELETE /annotations}
     *     </td>
     *   </tr>
     *   <tr>
     *       <td> {@link Scope#VOTE} </td> <td>
     *           {@code PUT /annotations/:id/upvote} <br>
     *           {@code PUT /annotations/:id/downvote} <br>
     *           {@code PUT /annotations/:id/unvote}</td>
     *   </tr>
     * </table>
     *
     * @param state       The state value to be returned with the code redirect
     * @param redirectUri The Redirect URI where the authorization code will be sent
     *                    as listened on the API Client management page.
     * @param scopes      The scopes requested for the access token
     *
     * @return A Mono emitting the authorization code URI.
     *
     * @see #authorizationCodeUriRequest()
     */
    @Contract("_, _, _, _ -> new")
    @NotNull
    AuthorizationCodeUriRequest authorizationCodeUri(
            @NotNull String state,
            @NotNull String redirectUri,
            @NotNull Scope scope,
            @NotNull Scope... scopes
    );

    /**
     * Creates a new {@link AccessTokenRequest.Builder} using the provided authorization code
     * from the {@link AuthorizationCodeUriRequest}.
     * <p>
     * This request requires the following parameters:
     * <ul>
     *     <li>authorization_code</li>
     *     <li>redirect_uri</li>
     *     <li>scope</li>
     * </ul>
     *
     * <p>
     * <b>This is required when making requests on behalf of a user. </b>
     *
     * @return The Access Token Request.
     *
     * @see #authorizationCodeUriRequest()
     *
     */
    @Contract("-> new")
    @NotNull
    AccessTokenRequest.Builder accessTokenRequest();

    /**
     * Creates a new {@link AccessTokenRequest} using the provided authorization code
     * from the {@link AuthorizationCodeUriRequest} and builds it.
     * <p>
     * <b>This is required when making requests on behalf of a user. </b>
     *
     * @param authorizationCode The authorization code received from the authorization
     *                          code URI request
     * @param redirectUri       The Redirect URI where the authorization code was sent
     *                          as listened on the API Client management page.
     * @param scope             The scopes requested for the access token
     *
     * @return A Mono emitting the GeniusAccessToken.
     *
     * @see #authorizationCodeUriRequest()
     */
    @Contract("_, _, _, _ -> new")
    @NotNull
    AccessTokenRequest accessToken(
            @NotNull String authorizationCode,
            @NotNull String redirectUri,
            @NotNull Scope scope,
            @NotNull Scope... scopes
    );

    /**
     * Creates a new {@link GetAccountRequest} that will
     * retrieve the authenticated user's account information.
     * <p>
     * Endpoint: {@code GET /account}
     * <p>
     * Required Scopes: {@link Scope#ME}
     *
     * @param format The text formats for the about me section
     *
     * @return The Get Account Request.
     */
    @Contract("_ -> new")
    @NotNull
    GetAccountRequest getAccount(@NotNull TextFormat... format);

    /**
     * Creates a new {@link GetAnnotationRequest.Builder} that will
     * be used to build a {@link GetAnnotationRequest} to
     * retrieve an {@link dev.spoocy.genius.model.Annotation}.
     * <p>
     * This request requires the following parameters:
     * <ul>
     *     <li>id</li>
     * </ul>
     *
     * <p>
     * This request can optionally have the following parameters:
     * <ul>
     *     <li>formats</li>
     * </ul>
     *
     * <p>
     * Endpoint: {@code GET /annotations/:id}
     *
     * @return A Request Builder.
     */
    @Contract("-> new")
    @NotNull
    GetAnnotationRequest.Builder annotation();

    /**
     * Builds a {@link GetAnnotationRequest} that will
     * retrieve an {@link dev.spoocy.genius.model.Annotation}.
     *
     * @param annotation_id The ID of the annotation to retrieve.
     * @param formats       The text formats for the annotation body.
     *
     * @return The Request.
     */
    @Contract("_, _ -> new")
    @NotNull
    GetAnnotationRequest getAnnotation(long annotation_id, @NotNull TextFormat... formats);

    /**
     * Creates a new {@link GetReferentsRequest.Builder} that will
     * be used to build a {@link GetReferentsRequest} to
     * retrieve {@link Referents} for a song or web page.
     * <p>
     * This request requires ONE of the following parameters:
     * <ul>
     *     <li>song_id</li>
     *     <li>web_page_id</li>
     * </ul>
     * The endpoint only accepts one of song_id or web_page_id, not both.
     *
     * <p>
     * This request can optionally have the following parameters:
     * <ul>
     *     <li>created_by_id</li>
     *     <li>text_format</li>
     *     <li>per_page</li>
     *     <li>page</li>
     * </ul>
     *
     * <p>
     * Endpoint: {@code GET /referents}
     *
     * @return The Get Referents Request Builder.
     */
    @Contract("-> new")
    @NotNull
    GetReferentsRequest.Builder referents();

    /**
     * Creates a new {@link GetReferentsRequest.Builder} that will
     * be used to build a {@link GetReferentsRequest} to
     * retrieve {@link Referents} for a song or web page.
     * <p>
     * This request can optionally have the following parameters:
     * <ul>
     *     <li>created_by_id</li>
     *     <li>text_format</li>
     *     <li>per_page</li>
     *     <li>page</li>
     * </ul>
     *
     * <p>
     * Endpoint: {@code GET /referents}
     *
     * @param song_id The ID of the song to retrieve.
     * @param perPage The number of referents to retrieve per page.
     * @param page    The page number to retrieve.
     * @param format  The text formats for the referents.
     *
     * @return The Get Referents Request.
     *
     * @see #referents()
     */
    @Contract("_, _, _, _ -> new")
    @NotNull
    GetReferentsRequest.Builder getReferentsBySongId(
            long song_id,
            int perPage,
            int page,
            @NotNull TextFormat... format
    );

    /**
     * Creates a new {@link GetReferentsRequest.Builder} that will
     * be used to build a {@link GetReferentsRequest} to
     * retrieve {@link Referents} for a song or web page.
     * <p>
     * This request can optionally have the following parameters:
     * <ul>
     *     <li>created_by_id</li>
     *     <li>text_format</li>
     *     <li>per_page</li>
     *     <li>page</li>
     * </ul>
     *
     * <p>
     * Endpoint: {@code GET /referents}
     *
     * @param web_page_id The ID of the web page to retrieve.
     * @param perPage     The number of referents to retrieve per page.
     * @param page        The page number to retrieve.
     * @param format      The text formats for the referents.
     *
     * @return The Get Referents Request.
     *
     * @see #referents()
     */
    @Contract("_, _, _, _ -> new")
    @NotNull
    GetReferentsRequest getReferentsByWebPage(long web_page_id, int perPage, int page, @NotNull TextFormat... format);

    /**
     * Creates a new {@link GetSongRequest} that will
     * be used to build a {@link GetSongRequest} to
     * retrieve a {@link dev.spoocy.genius.model.Song}.
     * <p>
     * This request requires the following parameters:
     * <ul>
     *     <li>id</li>
     * </ul>
     *
     * <p>
     * This request can optionally have the following parameters:
     * <ul>
     *     <li>formats</li>
     * </ul>
     *
     * <p>
     * Endpoint: {@code GET /songs/:id}
     *
     * @return The Get Song Request Builder.
     */
    @Contract("-> new")
    @NotNull
    GetSongRequest.Builder song();

    /**
     * Builds a {@link GetSongRequest} that will
     * retrieve a {@link dev.spoocy.genius.model.Song}.
     *
     * @param song_id The ID of the song to retrieve.
     * @param format  The text formats for the song lyrics and description.
     *
     * @return The Request.
     *
     * @see #song()
     */
    @Contract("_,_ -> new")
    @NotNull
    GetSongRequest getSong(long song_id, @NotNull TextFormat... format);

    /**
     * Creates a new {@link GetArtistRequest.Builder} that will
     * be used to build a {@link GetArtistRequest} to
     * retrieve an {@link dev.spoocy.genius.model.Artist}.
     * <p>
     * This request requires the following parameters:
     * <ul>
     *     <li>id</li>
     * </ul>
     *
     * <p>
     * This request can optionally have the following parameters:
     * <ul>
     *     <li>formats</li>
     * </ul>
     *
     * <p>
     * Endpoint: {@code GET /artists/:id}
     *
     * @return A Request Builder.
     */
    @Contract("-> new")
    @NotNull
    GetArtistRequest.Builder artist();

    /**
     * Builds a {@link GetArtistRequest} that will
     * retrieve an {@link dev.spoocy.genius.model.Artist}.
     *
     * @param artist_id The ID of the artist to retrieve.
     * @param format    The text formats for the artist description.
     *
     * @return The Request.
     */
    @Contract("_,_ -> new")
    @NotNull
    GetArtistRequest getArtist(long artist_id, @NotNull TextFormat... format);

    /**
     * Creates a new {@link GetArtistSongsRequest.Builder} that will
     * be used to build a {@link GetArtistSongsRequest} to
     * retrieve songs by an {@link dev.spoocy.genius.model.Artist}.
     * <p>
     * This request requires the following parameters:
     * <ul>
     *     <li>id</li>
     * </ul>
     *
     * <p>
     * This request can optionally have the following parameters:
     * <ul>
     *     <li>sort</li>
     *     <li>per_page</li>
     *     <li>page</li>
     * </ul>
     *
     * <p>
     * Endpoint: {@code GET /artists/:id/songs}
     *
     * @return The Get Artist Songs Request Builder.
     */
    GetArtistSongsRequest.Builder artistSongs();

    /**
     * Builds a {@link GetArtistSongsRequest} that will
     * retrieve songs by an {@link dev.spoocy.genius.model.Artist}.
     *
     * @param id      The ID of the artist to retrieve songs for.
     * @param sort    The sort order of the results.
     * @param perPage The number of results to return per page.
     * @param page    The page number to retrieve.
     *
     * @return The Request.
     */
    @Contract("_, _, _, _ -> new")
    @NotNull
    GetArtistSongsRequest getArtistSongs(long id, @NotNull Sort sort, int perPage, int page);


    /**
     * Creates a new {@link GetWebPagesLookupRequest.Builder} that will
     * retrieve web page information for a specific URL.
     * <p>
     * This request requires at least one of the following parameters:
     * <ul>
     *     <li>raw_annotatable_url</li>
     *     <li>canonical_url</li>
     *     <li>og_url</li>
     * </ul>
     * Provide as many of the variants of the URL as possible.
     *
     * <p>
     * Endpoint: {@code GET /web_pages/lookup}
     *
     * @return The Get Web Pages Lookup Request Builder.
     */
    @Contract("-> new")
    @NotNull
    GetWebPagesLookupRequest.Builder webPagesLookup();

    /**
     * Creates a new {@link GetWebPagesLookupRequest} that will
     * retrieve web page information for a specific URL.
     * <p>
     * Endpoint: {@code GET /web_pages/lookup}
     *
     * <p>
     * Provide as many of the following variants of the URL as possible: <br>
     * - raw_annotatable_url <br>
     * - canonical_url <br>
     * - og_url <br>
     *
     * @param raw_annotatable_url The raw annotatable URL.
     * @param canonical_url       The canonical URL.
     * @param og_url              The Open Graph URL.
     *
     * @return The Get Web Pages Lookup Request.
     *
     * @throws IllegalArgumentException if all URL parameters are null.
     */
    @Contract("_, _, _ -> new")
    @NotNull
    GetWebPagesLookupRequest getWebPage(
            @Nullable String raw_annotatable_url,
            @Nullable String canonical_url,
            @Nullable String og_url
    );

    /**
     * Creates a new {@link SearchRequest.Builder} that will
     * be used to build a {@link SearchRequest} to
     * search for songs, artists, and albums.
     * <p>
     * This request requires the following parameters:
     * <ul>
     *     <li>query</li>
     * </ul>
     *
     * <p>
     * Endpoint: {@code GET /search}
     *
     * @return The Search Request Builder.
     */
    @Contract("-> new")
    @NotNull
    SearchRequest.Builder search();

    /**
     * Builds a {@link SearchRequest}.
     *
     * @param query The search query.
     *
     * @return The Search Request.
     */
    @Contract("_ -> new")
    @NotNull
    SearchRequest search(@NotNull String query);

    /**
     * Creates a new {@link LyricsRequest.Builder} that will
     * be used to build a {@link LyricsRequest} to
     * retrieve {@link dev.spoocy.genius.model.Lyrics} for a song.
     * <p>
     * This request requires one of the following parameters:
     * <ul>
     *     <li>url -> "https://genius.com/Kendrick-lamar-humble-lyrics"</li>
     *     <li>path -> "Kendrick-lamar-humble-lyrics"</li>
     *     <li>query -> "Kendrick lamar humble"</li>
     * </ul>
     *
     *
     * @return The Lyrics Request Builder.
     */
    @Contract("-> new")
    @NotNull
    @Genius.Unofficial
    LyricsRequest.Builder lyrics();

    /**
     * Builds a {@link LyricsRequest} to search
     * for {@link dev.spoocy.genius.model.Lyrics} by a
     * specific URL.
     * <br>
     * E.g. https://genius.com/Wham-last-christmas-lyrics
     *
     * @param url   The URL of the song lyrics page.
     *
     * @return The Lyrics Request.
     */
    @Contract("_ -> new")
    @NotNull
    @Genius.Unofficial
    LyricsRequest getLyricsByUrl(@NotNull String url);

    /**
     * Builds a {@link LyricsRequest}to search
     * for {@link dev.spoocy.genius.model.Lyrics} by query.
     *
     * @param query The search query to find the song lyrics.
     *
     * @return The Lyrics Request.
     */
    @Contract("_ -> new")
    @NotNull
    @Genius.Unofficial
    LyricsRequest getLyricsByQuery(@NotNull String query);

    /**
     * Creates a new {@link SectionsSearchRequest.Builder} that will
     * be used to build a {@link SectionsSearchRequest} to
     * search for {@link dev.spoocy.genius.model.Sections}.
     * <p>
     * This request requires the following parameters:
     * <ul>
     *     <li>type</li>
     *     <li>query</li>
     *</ul>
     * <p>
     * This request can optionally have the following parameters:
     * <ul>
     *     <li>per_page</li>
     *     <li>page</li>
     * </ul>
     * Endpoint: {@code GET /api/search/:type}
     *
     * @return The Search Lyric Request Builder.
     */
    @Genius.Undocumented
    @Contract("-> new")
    @NotNull
    SectionsSearchRequest.Builder usearch();

    /**
     * Endpoint: {@code GET /api/search/song}
     * <p>
     * Type: {@link dev.spoocy.genius.core.http.SearchType#SONG}
     *
     * @see #usearch()
     */
    @Genius.Undocumented
    @Contract("_, _, _ -> new")
    @NotNull
    SectionsSearchRequest searchSong(@NotNull String query, int perPage, int page);

    /**
     * Endpoint: {@code GET /api/search/lyric}
     * <p>
     * Type: {@link dev.spoocy.genius.core.http.SearchType#LYRIC}
     *
     * @param query The search query.
     * @param perPage The number of results to return per page. (default: 10 | max: 50)
     * @param page The page number to retrieve.
     *
     * @see #usearch()
     */
    @Genius.Undocumented
    @Contract("_, _, _ -> new")
    @NotNull
    SectionsSearchRequest searchLyric(@NotNull String query, int perPage, int page);

    /**
     * Endpoint: {@code GET /api/search/artist}
     * <p>
     * Type: {@link dev.spoocy.genius.core.http.SearchType#ARTIST}
     *
     * @param query The search query.
     * @param perPage The number of results to return per page. (default: 10 | max: 50)
     * @param page The page number to retrieve.
     *
     * @see #usearch()
     */
    @Genius.Undocumented
    @Contract("_, _, _ -> new")
    @NotNull
    SectionsSearchRequest searchArtist(@NotNull String query, int perPage, int page);

    /**
     * Endpoint: {@code GET /api/search/album}
     * <p>
     * Type: {@link dev.spoocy.genius.core.http.SearchType#ALBUM}
     *
     * @param query The search query.
     * @param perPage The number of results to return per page. (default: 10 | max: 50)
     * @param page The page number to retrieve.
     *
     * @see #usearch()
     */
    @Genius.Undocumented
    @Contract("_, _, _ -> new")
    @NotNull
    SectionsSearchRequest searchAlbum(@NotNull String query, int perPage, int page);

    /**
     * Endpoint: {@code GET /api/search/video}
     * <p>
     * Type: {@link dev.spoocy.genius.core.http.SearchType#VIDEO}
     *
     * @param query The search query.
     * @param perPage The number of results to return per page. (default: 10 | max: 50)
     * @param page The page number to retrieve.
     *
     * @see #usearch()
     */
    @Genius.Undocumented
    @Contract("_, _, _ -> new")
    @NotNull
    SectionsSearchRequest searchVideo(@NotNull String query, int perPage, int page);

    /**
     * Endpoint: {@code GET /api/search/article}
     * <p>
     * Type: {@link dev.spoocy.genius.core.http.SearchType#ARTICLE}
     *
     * @param query The search query.
     * @param perPage The number of results to return per page. (default: 10 | max: 50)
     * @param page The page number to retrieve.
     *
     * @see #usearch()
     */
    @Genius.Undocumented
    @Contract("_, _, _ -> new")
    @NotNull
    SectionsSearchRequest searchArticle(@NotNull String query, int perPage, int page);

    /**
     * Endpoint: {@code GET /api/search/user}
     * <p>
     * Type: {@link dev.spoocy.genius.core.http.SearchType#USER}
     *
     * @param query The search query.
     * @param perPage The number of results to return per page. (default: 10 | max: 50)
     * @param page The page number to retrieve.
     *
     * @see #usearch()
     */
    @Genius.Undocumented
    @Contract("_, _, _ -> new")
    @NotNull
    SectionsSearchRequest searchUSer(@NotNull String query, int perPage, int page);

    /**
     * Endpoint: {@code GET /api/search/multi}
     * <p>
     * Type: {@link dev.spoocy.genius.core.http.SearchType#MULTI}
     *
     * @param query The search query.
     * @param perPage The number of results to return per page. (max: 5)
     * @param page The page number to retrieve.
     *
     * @see #usearch()
     */
    @Genius.Undocumented
    @Contract("_, _, _ -> new")
    @NotNull
    SectionsSearchRequest searchMulti(@NotNull String query, int perPage, int page);

    /**
     * Creates a new {@link GeniusClientBuilder}
     * to build a {@link GeniusClient} instance.
     *
     * @return A new GeniusClientBuilder.
     */
    @Contract("-> new")
    @NotNull
    static GeniusClientBuilder builder() {
        return new GeniusClientBuilder();
    }

}
