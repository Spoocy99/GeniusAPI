package dev.spoocy.genius.core.http;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.http.internal.*;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.utils.common.misc.Args;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONParserConfiguration;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractRequest<T> extends Mono<T> implements IRequest<T> {

    private final NettyHttpManager httpManager;
    private final Set<Header> headers;
    private final Set<Parameter> bodyParameters;
    private final ContentType contentType;
    private final URI uri;

    @Nullable
    private NettyEntity body;

    protected AbstractRequest(@NotNull Builder<T, ?> builder) {
        this.httpManager = builder.httpManager;
        this.headers = builder.headers;
        this.bodyParameters = builder.bodyParameters;
        this.contentType = builder.contentType;
        this.body = builder.body;

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder
                .port(builder.port)
                .scheme(Args.notNullOrEmpty(builder.scheme, "Scheme"))
                .host(Args.notNullOrEmpty(builder.host, "Host"))
                .path(Args.notNullOrEmpty(builder.path, "Path"),
                        new ArrayList<>(builder.pathParameters));

        if (!builder.queryParameters.isEmpty()) {
            uriBuilder.parameters(new ArrayList<>(builder.queryParameters));
        }

        try {
            this.uri = uriBuilder.build();
            GeniusClient.LOGGER.trace("Constructed URI: {}", this.uri.toString());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Could not build URI for request.", e);
        }
    }

    @Override
    public URI getUri() {
        return this.uri;
    }

    @Override
    public NettyHttpManager getHttpManager() {
        return this.httpManager;
    }

    @Override
    public List<Header> getHeaders() {
        return List.copyOf(this.headers);
    }

    @Override
    public ContentType getContentType() {
        return this.contentType;
    }

    @Override
    public @NotNull NettyEntity getBody() {
        if (this.body == null && this.contentType != null) {
            switch (contentType.getMimeType()) {
                case "application/json":
                    body = NettyEntity.jsonParameters(bodyParameters);
                    break;
                case "application/x-www-form-urlencoded":
                    body = NettyEntity.fromFormUrlEncoded(bodyParameters);
                    break;
            }

        }

        if (this.body == null) {
            // no body set and no content type to build from
            throw new IllegalStateException("Request body could not be built.");
        }

        return this.body;
    }

    /**
     * Executes the request and returns the FULL raw JSON response as a String.
     *
     * @return A {@link Mono} emitting the raw JSON response as a String.
     */
    @NotNull
    public Mono<String> json() {
        return execute().switchIfEmpty(Mono.just(""));
    }

    /**
     * @deprecated Use {@link #json()} instead.
     */
    @Deprecated
    public Mono<String> raw() {
        return json();
    }

    /**
     * An internal {@link Publisher#subscribe(Subscriber)} that will bypass
     * {@link Hooks#onLastOperator(Function)} pointcut.
     * <p>
     * In addition to behave as expected by {@link Publisher#subscribe(Subscriber)}
     * in a controlled manner, it supports direct subscribe-time {@link Context} passing.
     *
     * @param actual the {@link Subscriber} interested into the published sequence
     *
     * @see Publisher#subscribe(Subscriber)
     */
    @Override
    public void subscribe(@NotNull CoreSubscriber<? super T> actual) {
        try {
            AtomicReference<Subscription> upstreamRef = new AtomicReference<>();

            execute().subscribe(new CoreSubscriber<>() {

                @Override
                public void onSubscribe(@NotNull Subscription s) {
                    upstreamRef.set(s);
                    actual.onSubscribe(s);
                }

                @Override
                public void onNext(String s) {

                    try {
                        T parsed = parseResponse(s);
                        actual.onNext(parsed);
                    } catch (Throwable parseEx) {
                        // cancel upstream and propagate error to downstream
                        Subscription up = upstreamRef.get();
                        if (up != null) {
                            try {
                                up.cancel();
                            } catch (Throwable ignore) {
                            }
                        }
                        actual.onError(parseEx);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    actual.onError(t);
                }

                @Override
                public void onComplete() {
                    actual.onComplete();
                }

                @Override
                public @NotNull Context currentContext() {
                    return actual.currentContext();
                }
            });

        } catch (Throwable e) {
            actual.onError(e);
        }
    }

    /**
     * Executes the request and returns the raw JSON response as a String.
     *
     * @return A {@link Mono} emitting the raw JSON response as a String.
     */
    private Mono<String> execute() {
            return execute0()
                    .switchIfEmpty(
                            Mono.error(new GeniusApiException("No response received from API."))
                    )
                    .flatMap(response -> {
                        if (response == null || response.isEmpty()) {
                            return Mono.error(new GeniusApiException("Empty response received from API."));
                        }
                        return Mono.just(response);
                    });
    }

    /**
     * Executes the request and returns the raw JSON response as a String.
     * <p>
     * This method is intended to be implemented by subclasses to define the specific HTTP method
     * (GET, POST, PUT, DELETE) and how the request should be executed.
     *
     * @return A {@link Mono} emitting the raw JSON response as a String.
     */
    @NotNull
    protected abstract Mono<String> execute0();

    /**
     * Parses the raw JSON response string into an instance of type T.
     *
     * @param response The raw JSON response as a String.
     *
     * @return An instance of type T representing the parsed response.
     *
     * @throws GeniusApiException if parsing fails or if the response indicates an error.
     */
    @NotNull
    protected abstract T parseResponse(@NotNull String response) throws GeniusApiException;

    /**
     * Executes a POST request on the current {@link NettyHttpManager} using the
     * configured URI, headers, and body.
     *
     * @return A {@link Mono} emitting the raw JSON response as a String.
     */
    @NotNull
    protected Mono<String> executePOST() {
        return this.httpManager.post(
                this.uri,
                this.headers.toArray(Header[]::new),
                this.getBody()
        );
    }

    /**
     * Executes a GET request on the current {@link NettyHttpManager} using the
     * configured URI and headers.
     *
     * @return A {@link Mono} emitting the raw JSON response as a String.
     */
    @NotNull
    protected Mono<String> executeGET() {
        return this.httpManager.get(
                this.uri,
                this.headers.toArray(Header[]::new)
        );
    }

    /**
     * Executes a PUT request on the current {@link NettyHttpManager} using the
     * configured URI, headers, and body.
     *
     * @return A {@link Mono} emitting the raw JSON response as a String.
     */
    @NotNull
    protected Mono<String> executePUT() {
        return this.httpManager.put(
                this.uri,
                this.headers.toArray(Header[]::new),
                this.getBody()
        );
    }

    /**
     * Executes a DELETE request on the current {@link NettyHttpManager} using the
     * configured URI and headers.
     *
     * @return A {@link Mono} emitting the raw JSON response as a String.
     */
    @NotNull
    protected Mono<String> executeDELETE() {
        return this.httpManager.delete(
                this.uri,
                this.headers.toArray(Header[]::new)
        );
    }

    public static abstract class Builder<T, B extends Builder<T, ?>> {

        private final Set<Parameter> pathParameters = new HashSet<>();
        private final Set<Parameter> queryParameters = new HashSet<>();
        private final Set<Parameter> bodyParameters = new HashSet<>();
        private final Set<Header> headers = new LinkedHashSet<>();

        protected NettyHttpManager httpManager = GeniusClient.DEFAULT_HTTP_MANAGER;
        protected String scheme = GeniusClient.DEFAULT_REQUEST_SCHEME;
        protected String host = GeniusClient.DEFAULT_API_BASE;
        protected int port = GeniusClient.DEFAULT_REQUEST_PORT;

        protected ContentType contentType = null;
        protected String path = null;
        protected NettyEntity body = null;

        public Builder() {

        }

        public B pathParameter(@NotNull String name, @NotNull String value) {
            this.pathParameters.add(
                    new Parameter(name, value)
            );
            return instance();
        }

        public B queryParameter(@NotNull String name, @NotNull String value) {
            this.queryParameters.add(
                    new Parameter(name, value)
            );
            return instance();
        }

        public B bodyParameter(@NotNull String name, @NotNull String value) {
            this.bodyParameters.add(
                    new Parameter(name, value)
            );
            return instance();
        }

        public B header(@NotNull String name, @NotNull String value) {
            this.headers.add(
                    new Header(name, value)
            );
            return instance();
        }

        public B httpManager(@NotNull NettyHttpManager httpManager) {
            this.httpManager = httpManager;
            return instance();
        }

        public B scheme(@NotNull String scheme) {
            this.scheme = scheme;
            return instance();
        }

        public B host(@NotNull String host) {
            this.host = host;
            return instance();
        }

        public B port(int port) {
            this.port = port;
            return instance();
        }

        public B path(@NotNull String path) {
            this.path = path;
            return instance();
        }

        public B contentType(@NotNull ContentType contentType) {
            this.contentType = contentType;
            return instance();
        }

        public B body(@NotNull NettyEntity body) {
            this.body = body;
            return instance();
        }

        public abstract IRequest<T> build();

        protected abstract B instance();

    }

}
