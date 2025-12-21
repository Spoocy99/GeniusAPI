package dev.spoocy.genius.core.http;

import dev.spoocy.genius.exception.GeniusApiException;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface IHttpManager {

    HttpHost getProxy();

    UsernamePasswordCredentials getProxyCredentials();

    String getUserAgent();

    int getMaxCacheEntries();

    int getMaxCacheEntrySize();

    boolean isCacheShared();

    int getConnectionRequestTimeout();

    int getSocketTimeout();

    /**
     * Executes a GET request to the specified URI with the provided headers.
     *
     * @param uri     The target URI for the GET request.
     * @param headers An array of headers to include in the request.
     *
     * @return The response body as a String.
     *
     * @throws IOException        If an I/O error occurs during the request.
     * @throws GeniusApiException If the Genius API returns an error response.
     * @throws ParseException     If there is an error parsing the response.
     */
    String get(@NotNull URI uri, @NotNull Header[] headers)
            throws IOException, GeniusApiException, ParseException;

    /**
     * Executes a POST request to the specified URI with the provided headers and body.
     *
     * @param uri     The target URI for the POST request.
     * @param headers An array of headers to include in the request.
     * @param body    The HttpEntity representing the body of the POST request.
     *
     * @return The response body as a String.
     *
     * @throws IOException        If an I/O error occurs during the request.
     * @throws GeniusApiException If the Genius API returns an error response.
     */
    String post(@NotNull URI uri, @NotNull Header[] headers, @NotNull HttpEntity body)
            throws IOException, GeniusApiException;

    /**
     * Executes a PUT request to the specified URI with the provided headers and body.
     *
     * @param uri     The target URI for the PUT request.
     * @param headers An array of headers to include in the request.
     * @param body    The HttpEntity representing the body of the PUT request.
     *
     * @return The response body as a String.
     *
     * @throws IOException        If an I/O error occurs during the request.
     * @throws GeniusApiException If the Genius API returns an error response.
     */
    String put(@NotNull URI uri, @NotNull Header[] headers, @NotNull HttpEntity body)
            throws IOException, GeniusApiException;

    /**
     * Executes a DELETE request to the specified URI with the provided headers.
     *
     * @param uri     The target URI for the DELETE request.
     * @param headers An array of headers to include in the request.
     *
     * @return The response body as a String.
     *
     * @throws IOException        If an I/O error occurs during the request.
     * @throws GeniusApiException If the Genius API returns an error response.
     */
    String delete(@NotNull URI uri, @NotNull Header[] headers)
            throws IOException, GeniusApiException;
}
