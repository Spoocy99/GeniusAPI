package dev.spoocy.genius.core.http;

import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.CorePublisher;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

/**
 * A representation of an HTTP request that can be executed to get a response of type T.
 *
 * @param <T> the type of the response expected from the request
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface IRequest<T> extends CorePublisher<T> {

    /**
     * Gets the {@link IHttpManager} used for this request.
     *
     * @return the HTTP manager
     */
    IHttpManager getHttpManager();

    /**
     * Gets the HTTP method of the request.
     *
     * @return the HTTP method
     */
    URI getUri();

    /**
     * Gets the headers of the request.
     *
     * @return the list of headers
     */
    List<Header> getHeaders();

    /**
     * Gets the body of the request.
     *
     * @return the HTTP entity representing the body
     */
    HttpEntity getBody();

    /**
     * Gets the content type of the request.
     *
     * @return the content type
     */
    ContentType getContentType();

    /**
     * Executes the request and returns the raw JSON response as a String.
     *
     * @return A {@link Mono} emitting the raw JSON response as a String.
     */
    Mono<String> raw();

    /**
     * An internal {@link Publisher#subscribe(Subscriber)} that will bypass
     * {@link Hooks#onLastOperator(Function)} pointcut.
     * <p>
     * In addition to behave as expected by {@link Publisher#subscribe(Subscriber)}
     * in a controlled manner, it supports direct subscribe-time {@link Context} passing.
     *
     * @param subscriber the {@link Subscriber} interested into the published sequence
     *
     * @see Publisher#subscribe(Subscriber)
     */
    @Override
    void subscribe(@NotNull CoreSubscriber<? super T> subscriber);

}
