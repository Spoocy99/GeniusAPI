package dev.spoocy.genius.core.http;

import dev.spoocy.genius.core.http.internal.ContentType;
import dev.spoocy.genius.core.http.internal.Header;
import dev.spoocy.genius.core.http.internal.NettyEntity;
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
     * Gets the {@link NettyHttpManager} used for this request.
     *
     * @return the HTTP manager
     */
    NettyHttpManager getHttpManager();

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
     * Gets the content type of the request.
     *
     * @return the content type
     */
    ContentType getContentType();

    /**
     * Gets the body of the request.
     *
     * @return the HTTP entity representing the body
     */
    NettyEntity getBody();

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
