package dev.spoocy.genius.core.http;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.exception.*;
import dev.spoocy.utils.common.log.ILogger;
import dev.spoocy.utils.common.misc.Args;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.cache.HttpCacheContext;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.cache.CacheConfig;
import org.apache.hc.client5.http.impl.cache.CachingHttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Class to manage HTTP requests to the Genius API.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GeniusHttpManager implements IHttpManager {

    private static final ILogger LOGGER = ILogger.forThisClass();

    private final HttpClientConnectionManager connectionManager;
    private final HttpHost proxy;
    private final UsernamePasswordCredentials proxyCredentials;
    private final String userAgent;
    private final int cacheMaxEntries;
    private final int cacheMaxEntrySize;
    private final boolean cacheShared;
    private final int connectionRequestTimeout;
    private final int socketTimeout;

    private final CloseableHttpClient httpClient, cacheHttpClient;

    public GeniusHttpManager(@NotNull Builder builder) {
        this.connectionManager = builder.connectionManager;
        this.proxy = builder.proxy;
        this.proxyCredentials = builder.proxyCredentials;
        this.userAgent = builder.userAgent;
        this.cacheMaxEntries = builder.cacheMaxEntries;
        this.cacheMaxEntrySize = builder.cacheMaxEntrySize;
        this.cacheShared = builder.cacheShared;
        this.connectionRequestTimeout = builder.connectionRequestTimeout;
        this.socketTimeout = builder.socketTimeout;

        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        if (this.proxy != null && this.proxyCredentials != null) {
            credentialsProvider.setCredentials(
                    new AuthScope(null,
                            this.proxy.getHostName(),
                            this.proxy.getPort(),
                            null,
                            this.proxy.getSchemeName()),

                    this.proxyCredentials
            );
        }

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setCookieSpec(StandardCookieSpec.STRICT)
                .setConnectionRequestTimeout(builder.connectionRequestTimeout > 0
                        ? Timeout.ofMilliseconds(builder.connectionRequestTimeout)
                        : RequestConfig.DEFAULT.getConnectionRequestTimeout())
                .setResponseTimeout(builder.socketTimeout > 0
                        ? Timeout.ofMilliseconds(builder.socketTimeout)
                        : RequestConfig.DEFAULT.getResponseTimeout())
                .build();

        HttpRequestRetryStrategy retryStrategy = new GeniusHttpRequestRetryStrategy(
                builder.maxRetries,
                Timeout.ofMilliseconds(builder.retryIntervalMillis),
                List.of(HttpStatus.SC_SERVICE_UNAVAILABLE)
        );

        this.httpClient = HttpClients
                .custom()
                .disableContentCompression()
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultRequestConfig(requestConfig)
                .setRetryStrategy(retryStrategy)
                .setConnectionManager(this.connectionManager)
                .setProxy(this.proxy)
                .setUserAgent(this.userAgent)
                .build();

        CacheConfig cacheConfig = CacheConfig.custom()
                .setMaxCacheEntries(this.cacheMaxEntries)
                .setMaxObjectSize(this.cacheMaxEntrySize)
                .setSharedCache(this.cacheShared)
                .build();

        this.cacheHttpClient = CachingHttpClients
                .custom()
                .setCacheConfig(cacheConfig)
                .disableContentCompression()
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultRequestConfig(requestConfig)
                .setRetryStrategy(retryStrategy)
                .setConnectionManager(this.connectionManager)
                .setProxy(this.proxy)
                .setUserAgent(this.userAgent)
                .build();
    }

    @Override
    public HttpHost getProxy() {
        return this.proxy;
    }

    @Override
    public UsernamePasswordCredentials getProxyCredentials() {
        return this.proxyCredentials;
    }

    @Override
    public String getUserAgent() {
        return this.userAgent;
    }

    @Override
    public int getMaxCacheEntries() {
        return this.cacheMaxEntries;
    }

    @Override
    public int getMaxCacheEntrySize() {
        return this.cacheMaxEntrySize;
    }

    @Override
    public boolean isCacheShared() {
        return this.cacheShared;
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
    public String get(@NotNull URI uri, @NotNull Header[] headers)
            throws IOException, GeniusApiException, ParseException {

        final HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeaders(headers);
        LOGGER.trace("POST Request to URI: {}\nHeaders: {}",
                uri,
                Arrays.toString(headers)
        );

        // using caching http client for GET requests
        String responseBody = execute(this.cacheHttpClient, httpGet);
        httpGet.reset();
        return responseBody;
    }

    @Override
    public String post(@NotNull URI uri, @NotNull Header[] headers, @NotNull HttpEntity body)
            throws IOException, GeniusApiException {

        final ClassicHttpRequest httpPost = new HttpPost(uri);
        httpPost.setHeaders(headers);
        httpPost.setEntity(body);
        LOGGER.trace("POST Request to URI: {}\nHeaders: {}\nBody: {}",
                uri,
                Arrays.toString(headers),
                body.toString()
        );

        return execute(this.httpClient, httpPost);
    }

    @Override
    public String put(@NotNull URI uri, @NotNull Header[] headers, @NotNull HttpEntity body)
            throws IOException, GeniusApiException {

        final ClassicHttpRequest httpPut = new HttpPut(uri);
        httpPut.setHeaders(headers);
        httpPut.setEntity(body);
        LOGGER.trace("PUT Request to URI: {}\nHeaders: {}\nBody: {}",
                uri,
                Arrays.toString(headers),
                body.toString()
        );

        return execute(this.httpClient, httpPut);
    }

    @Override
    public String delete(@NotNull URI uri, @NotNull Header[] headers)
            throws IOException, GeniusApiException {

        final ClassicHttpRequest httpDelete = new HttpDelete(uri);
        httpDelete.setHeaders(headers);
        LOGGER.trace("DELETE Request to URI: {}\nHeaders: {}",
                uri,
                Arrays.toString(headers)
        );

        return execute(this.httpClient, httpDelete);
    }

    /**
     * Executes the given HTTP request using the provided HttpClient.
     *
     * @param httpClient The HttpClient to use for executing the request.
     * @param method     The HTTP request to execute.
     *
     * @return The response body as a String.
     *
     * @throws IOException        If an I/O error occurs during the request.
     * @throws GeniusApiException If the Genius API returns an error response.
     */
    private String execute(@NotNull CloseableHttpClient httpClient, @NotNull ClassicHttpRequest method)
            throws IOException, GeniusApiException {

        HttpCacheContext context = HttpCacheContext.create();
        ResponseHandler responseHandler = new ResponseHandler();
        String response;

        try {
            response = httpClient.execute(method, context, responseHandler);
        } catch (IOException e) {

            if (e.getCause() instanceof GeniusApiException) {
                throw (GeniusApiException) e.getCause();
            }

            throw e;
        }

        return response;
    }

    /**
     * Creates a URI from the given string.
     *
     * @param uriString The string representation of the URI.
     *
     * @return The URI object, or null if there was a syntax error.
     */
    public static URI makeUri(@NotNull String uriString) {
        try {
            return new URI(uriString);
        } catch (URISyntaxException e) {
            LOGGER.error("URI Syntax Exception for \"" + uriString + "\"");
            return null;
        }
    }

    /**
     * Custom response handler to process HTTP responses and handle errors.
     */
    private static class ResponseHandler implements HttpClientResponseHandler<String> {

        private static final ILogger LOGGER = ILogger.forClass(ResponseHandler.class);

        @Override
        public String handleResponse(ClassicHttpResponse httpResponse)
                throws GeniusApiException, IOException, ParseException {

            final String responseBody = httpResponse.getEntity() != null
                    ? EntityUtils.toString(httpResponse.getEntity(), "UTF-8")
                    : null;

            String errorMessage = httpResponse.getReasonPhrase();

            LOGGER.trace("Http Response (Code {}, Message: {}): {}",
                    httpResponse.getCode(),
                    httpResponse.getReasonPhrase(),
                    (responseBody != null && !responseBody.isEmpty()) ? "Received Response" : "Empty Response"
            );

            if (responseBody != null && !responseBody.isEmpty()) {
                // try and extract error message
                JSONObject json = new JSONObject(responseBody);

                if (json.has("meta")) {
                    JSONObject meta = json.getJSONObject("meta");

                    if (meta.has("message")) {
                        errorMessage = meta.getString("message");
                    }
                } else {
                    LOGGER.warn("No 'meta' object found in response body when trying to extract error message.");
                }
            }

            switch (httpResponse.getCode()) {
                case HttpStatus.SC_BAD_REQUEST:
                    throw new BadRequestException(errorMessage);
                case HttpStatus.SC_FORBIDDEN:
                    throw new ForbiddenException(errorMessage);
                case HttpStatus.SC_TOO_MANY_REQUESTS:
                    throw new RateLimitException(errorMessage);
            }

            if (httpResponse.getCode() >= 400 && httpResponse.getCode() < 500) {
                throw new BadRequestException(errorMessage);

            } else if (httpResponse.getCode() >= 500) {
                throw new InternalServerErrorException(errorMessage);
            }

            return responseBody;

        }

    }

    public static class Builder {
        private HttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        private HttpHost proxy;
        private UsernamePasswordCredentials proxyCredentials;
        private String userAgent = GeniusClient.DEFAULT_USER_AGENT;
        private int cacheMaxEntries = CacheConfig.DEFAULT_MAX_CACHE_ENTRIES;
        private int cacheMaxEntrySize = CacheConfig.DEFAULT_MAX_OBJECT_SIZE_BYTES;
        private boolean cacheShared = false;
        private int connectionRequestTimeout = -1;
        private int socketTimeout = -1;

        /**
         * Settings for {@link GeniusHttpRequestRetryStrategy}
         */
        private int maxRetries = 1;
        private int retryIntervalMillis = 2000;

        /**
         * Sets a custom connection manager.
         *
         * @param connectionManager the HttpClientConnectionManager to use
         *
         * @return The Builder instance for chaining.
         */
        public Builder connectionManager(@NotNull HttpClientConnectionManager connectionManager) {
            this.connectionManager = connectionManager;
            return this;
        }

        /**
         * Sets a proxy for the HTTP client.
         *
         * @param proxy the HttpHost representing the proxy
         *
         * @return The Builder instance for chaining.
         */
        public Builder proxy(@NotNull HttpHost proxy) {
            this.proxy = proxy;
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
         * Sets proxy credentials for the HTTP client.
         *
         * @param proxyCredentials the UsernamePasswordCredentials for the proxy
         *
         * @return The Builder instance for chaining.
         */
        public Builder proxyCredentials(@NotNull UsernamePasswordCredentials proxyCredentials) {
            this.proxyCredentials = proxyCredentials;
            return this;
        }

        /*  Sets the maximum number of cache entries.
         *
         * @param cacheMaxEntries the maximum number of cache entries
         *
         * @return The Builder instance for chaining.
         */
        public Builder cacheMaxEntries(int cacheMaxEntries) {
            this.cacheMaxEntries = cacheMaxEntries;
            return this;
        }

        /**
         * Sets the maximum object size for the cache.
         *
         * @param cacheMaxObjectSize the maximum object size in bytes
         *
         * @return The Builder instance for chaining.
         */
        public Builder cacheMaxObjectSize(int cacheMaxObjectSize) {
            this.cacheMaxEntrySize = cacheMaxObjectSize;
            return this;
        }

        /**
         * Sets whether the cache is shared.
         *
         * @param cacheShared true if the cache is shared, false otherwise
         *
         * @return The Builder instance for chaining.
         */
        public Builder cacheShared(boolean cacheShared) {
            this.cacheShared = cacheShared;
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
        public GeniusHttpManager build() {
            return new GeniusHttpManager(this);
        }

    }

}
