package dev.spoocy.genius.core.http;

import dev.spoocy.genius.core.http.internal.Header;
import dev.spoocy.genius.core.http.internal.NettyEntity;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface NettyHttpManager {

    boolean isProxy();

    String getUserAgent();

    int getConnectionRequestTimeout();

    int getSocketTimeout();

    /**
     * Executes an HTTP request with the specified method, URI, headers, and body.
     *
     * @param uri the URI to send the request to
     * @param headers the headers to include in the request
     *
     * @return a Mono that emits the response body as a String when the request is
     * completed, or an error if the request fails
     */
    @NotNull
    Mono<String> get(@NotNull URI uri, @NotNull Header[] headers);

    /**
     * Executes an HTTP POST request with the specified URI, headers, and body.
     *
     * @param uri the URI to send the request to
     * @param headers the headers to include in the request
     * @param body the body of the request as a NettyEntity
     *
     * @return a Mono that emits the response body as a String when the request is
     * completed, or an error if the request fails
     */
    @NotNull
    Mono<String> post(@NotNull URI uri, @NotNull Header[] headers, @NotNull NettyEntity body);

    /**
     * Executes an HTTP PUT request with the specified URI, headers, and body.
     *
     * @param uri the URI to send the request to
     * @param headers the headers to include in the request
     * @param body the body of the request as a NettyEntity
     *
     * @return a Mono that emits the response body as a String when the request is
     * completed, or an error if the request fails
     */
    @NotNull
    Mono<String> put(@NotNull URI uri, @NotNull Header[] headers, @NotNull NettyEntity body);

    /**
     * Executes an HTTP DELETE request with the specified URI and headers.
     *
     * @param uri the URI to send the request to
     * @param headers the headers to include in the request
     *
     * @return a Mono that emits the response body as a String when the request is
     * completed, or an error if the request fails
     */
    @NotNull
    Mono<String> delete(@NotNull URI uri, @NotNull Header[] headers);
}
