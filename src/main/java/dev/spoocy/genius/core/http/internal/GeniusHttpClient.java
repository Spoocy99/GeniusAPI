package dev.spoocy.genius.core.http.internal;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.http.NettyHttpManager;
import dev.spoocy.genius.core.http.NettyResponseHandler;
import dev.spoocy.genius.exception.*;
import dev.spoocy.utils.common.log.ILogger;
import io.netty.channel.ChannelOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.ProxyProvider;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class to manage HTTP requests to the Genius API.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GeniusHttpClient implements NettyHttpManager {

    private static final ILogger LOGGER = ILogger.forThisClass();

    private final ConnectionProvider connectionProvider;
    private final NettyResponseHandler<String> responseHandler;
    private final Consumer<? super ProxyProvider.TypeSpec> proxyOptions;
    private final String userAgent;
    private final int connectionRequestTimeout;
    private final int socketTimeout;

    private final GeniusHttpRequestRetryStrategy retryStrategy;
    private final HttpClient httpClient;

    public GeniusHttpClient(@NotNull Builder builder) {
        this.connectionProvider = builder.connectionProvider;
        this.responseHandler = builder.responseHandler;
        this.proxyOptions = builder.proxyOptions;
        this.userAgent = builder.userAgent;
        this.connectionRequestTimeout = builder.connectionRequestTimeout;
        this.socketTimeout = builder.socketTimeout;

        this.retryStrategy = new GeniusHttpRequestRetryStrategy(
                builder.maxRetries,
                java.time.Duration.ofMillis(builder.retryIntervalMillis),
                List.of(GeniusApiException.class)
        );

        HttpClient client = HttpClient
                .create(this.connectionProvider)
                .compress(false)
                .headers(headers -> headers.add("User-Agent", this.userAgent));

        if (this.proxyOptions != null) {
            client = client.proxy(this.proxyOptions);
        }

        if (this.connectionRequestTimeout > 0) {
            client = client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.connectionRequestTimeout);
        }

        if (this.socketTimeout > 0) {
            client = client.option(ChannelOption.SO_TIMEOUT, this.socketTimeout);
        }

        this.httpClient = client;
    }

    @Override
    public boolean isProxy() {
        return this.proxyOptions != null;
    }

    @Override
    public String getUserAgent() {
        return this.userAgent;
    }

    @Override
    public int getConnectionRequestTimeout() {
        return this.connectionRequestTimeout;
    }

    @Override
    public int getSocketTimeout() {
        return this.socketTimeout;
    }

    @Override
    public @NotNull Mono<String> get(@NotNull URI uri, @NotNull Header[] headers) {
        return newRequest(headers, null)
                .get()
                .uri(uri.toString())
                .responseSingle(this.responseHandler::handle)
                .retryWhen(this.retryStrategy.reactor())
                .doOnSubscribe(subscription -> LOGGER.trace("GET Request to URI: {}\nHeaders: {}",
                        uri,
                        Arrays.toString(headers)
                ))
                .doOnError(throwable -> LOGGER.error("GET Request to URI: {} failed with error: {}",
                        uri,
                        throwable.getMessage()
                ));
    }

    @Override
    public @NotNull Mono<String> post(@NotNull URI uri, @NotNull Header[] headers, @NotNull NettyEntity body) {
        return newRequest(headers, body)
                .post()
                .uri(uri.toString())
                .send(body.getBody())
                .responseSingle(this.responseHandler::handle)
                .retryWhen(this.retryStrategy.reactor())
                .doOnSubscribe(subscription -> LOGGER.trace("POST Request to URI: {}\nHeaders: {}\nBody: {}",
                        uri,
                        Arrays.toString(headers),
                        body.toString()
                ))
                .doOnError(throwable -> LOGGER.error("POST Request to URI: {} failed with error: {}",
                        uri,
                        throwable.getMessage()
                ));
    }

    @Override
    public @NotNull Mono<String> put(@NotNull URI uri, @NotNull Header[] headers, @NotNull NettyEntity body) {
        return newRequest(headers, body)
                .put()
                .uri(uri.toString())
                .send(body.getBody())
                .responseSingle(this.responseHandler::handle)
                .retryWhen(this.retryStrategy.reactor())
                .doOnSubscribe(subscription -> LOGGER.trace("PUT Request to URI: {}\nHeaders: {}\nBody: {}",
                        uri,
                        Arrays.toString(headers),
                        body.toString()
                ))
                .doOnError(throwable -> LOGGER.error("PUT Request to URI: {} failed with error: {}",
                        uri,
                        throwable.getMessage()
                ));
    }

    @Override
    public @NotNull Mono<String> delete(@NotNull URI uri, @NotNull Header[] headers) {
        return newRequest(headers, null)
                .delete()
                .uri(uri.toString())
                .responseSingle(this.responseHandler::handle)
                .retryWhen(this.retryStrategy.reactor())
                .doOnSubscribe(subscription -> LOGGER.trace("DELETE Request to URI: {}\nHeaders: {}",
                        uri,
                        Arrays.toString(headers)
                ))
                .doOnError(throwable -> LOGGER.error("DELETE Request to URI: {} failed with error: {}",
                        uri,
                        throwable.getMessage()
                ));
    }

    private HttpClient newRequest(@NotNull Header[] headers, @Nullable NettyEntity body) {
        return this.httpClient
                .headers(headerBuilder -> {

                    if (body != null) {
                        body.applyHaders(headerBuilder);
                    }

                    for (Header header : headers) {
                        headerBuilder.add(header.getName(), header.getValue());
                    }
                });
    }

    public static class Builder {
        private ConnectionProvider connectionProvider = ConnectionProvider.create("genius-http-connection-pool");
        private NettyResponseHandler<String> responseHandler = new GeniusResponseHandler();
        private Consumer<? super ProxyProvider.TypeSpec> proxyOptions;
        private String userAgent = GeniusClient.DEFAULT_USER_AGENT;
        private int connectionRequestTimeout = -1;
        private int socketTimeout = -1;

        /**
         * Settings for {@link GeniusHttpRequestRetryStrategy}
         */
        private int maxRetries = 1;
        private int retryIntervalMillis = 2000;

        /**
         * Sets a custom {@link ConnectionProvider} for the HTTP client.
         *
         * @param connectionProvider the ConnectionProvider to use
         *
         * @return The Builder instance for chaining.
         */
        public Builder connectionProvider(@NotNull ConnectionProvider connectionProvider) {
            this.connectionProvider = connectionProvider;
            return this;
        }

        /**
         * Sets a custom {@link NettyResponseHandler} for processing HTTP responses.
         *
         * @param responseHandler the NettyResponseHandler to use
         *
         * @return The Builder instance for chaining.
         */
        public Builder responseHandler(@NotNull NettyResponseHandler<String> responseHandler) {
            this.responseHandler = responseHandler;
            return this;
        }

        /**
         * Sets a proxy for the HTTP client.
         *
         * @param proxyOptions the proxy options consumer
         *
         * @return The Builder instance for chaining.
         */
        public Builder proxy(@NotNull Consumer<? super ProxyProvider.TypeSpec> proxyOptions) {
            this.proxyOptions = proxyOptions;
            return this;
        }

        /**
         * Sets the User-Agent header for the HTTP client.
         */
        public Builder userAgent(@NotNull String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        /**
         * Sets the connection request timeout.
         *
         * @param connectionRequestTimeout the connection request timeout in milliseconds
         *
         * @return The Builder instance for chaining.
         */
        public Builder connectionRequestTimeout(int connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
            return this;
        }

        /**
         * Sets the socket timeout.
         *
         * @param socketTimeout the socket timeout in milliseconds
         *
         * @return The Builder instance for chaining.
         */
        public Builder socketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        /**
         * Sets the maximum number of retries for the retry strategy.
         *
         * @param maxRetries the maximum number of retries
         *
         * @return The Builder instance for chaining.
         */
        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        /**
         * Sets the retry interval in milliseconds for the retry strategy.
         *
         * @param retryIntervalMillis the retry interval in milliseconds
         *
         * @return The Builder instance for chaining.
         */
        public Builder retryIntervalMillis(int retryIntervalMillis) {
            this.retryIntervalMillis = retryIntervalMillis;
            return this;
        }

        /**
         * Builds the GeniusHttpManager instance.
         *
         * @return A new GeniusHttpManager instance.
         */
        public GeniusHttpClient build() {
            return new GeniusHttpClient(this);
        }

    }

}
