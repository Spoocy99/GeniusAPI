package dev.spoocy.genius.core.http;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.utils.common.misc.Args;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractRequest<T> extends Mono<T> implements IRequest<T> {

    private final IHttpManager httpManager;
    private final List<Header> headers;
    private final List<NameValuePair> bodyParameters;
    private final ContentType contentType;
    private final URI uri;

    @Nullable
    private HttpEntity body;

    protected AbstractRequest(@NotNull Builder<T, ?> builder) {
        this.httpManager = builder.httpManager;
        this.headers = builder.getHeadersAsHeaderList();
        this.bodyParameters = builder.getBodyParametersAsNameValuePairs();
        this.contentType = builder.contentType;
        this.body = builder.body;

        String builtPath = Args.notNullOrEmpty(builder.path, "Path");

        if (!builder.pathParameters.isEmpty()) {
            for (NameValuePair nameValuePair : builder.getPathParametersAsNameValuePairs()) {
                String key = "\\{" + nameValuePair.getName() + "\\}";
                String value = nameValuePair.getValue();
                builtPath = builtPath.replaceAll(key, value);
            }
        }

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder
                .setPort(builder.port)
                .setScheme(Args.notNullOrEmpty(builder.scheme, "Scheme"))
                .setHost(Args.notNullOrEmpty(builder.host, "Host"))
                .setPath(builtPath);

        if (!builder.queryParameters.isEmpty()) {
            uriBuilder.setParameters(builder.getQueryParametersAsNameValuePairs());
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
    public IHttpManager getHttpManager() {
        return this.httpManager;
    }

    @Override
    public List<Header> getHeaders() {
        return this.headers;
    }

    @Override
    public @NotNull HttpEntity getBody() {
        if (this.body == null && this.contentType != null) {
            switch (contentType.getMimeType()) {
                case "application/json":
                    body = new StringEntity(
                            bodyParametersToJson(bodyParameters),
                            ContentType.APPLICATION_JSON);
                    break;
                case "application/x-www-form-urlencoded":
                    body = new UrlEncodedFormEntity(bodyParameters);
                    break;
            }

        }

        if (this.body == null) {
            // no body set and no content type to build from
            throw new IllegalStateException("Request body could not be built.");
        }

        return this.body;
    }

    @Override
    public ContentType getContentType() {
        return this.contentType;
    }

    private static final Set<String> KEEP_DATA_TYPES = java.util.Set.of(
            "collaborative", "device_ids", "ids", "insert_before", "offset",
            "play", "position", "position_ms", "public", "range_length",
            "range_start", "tracks", "uris"
    );

    /**
     * Converts body parameters to a JSON string, preserving data types for specific parameters.
     *
     * @param bodyParameters The list of body parameters as NameValuePair.
     *
     * @return A JSON string representation of the body parameters.
     */
    public String bodyParametersToJson(@NotNull List<NameValuePair> bodyParameters) {
        JSONObject jsonObject = new JSONObject();

        for (NameValuePair nameValuePair : bodyParameters) {
            String name = nameValuePair.getName();
            String value = nameValuePair.getValue();

            if (KEEP_DATA_TYPES.contains(name)) {

                try {
                    // Try to parse the value as JSON (to handle arrays, booleans, numbers, etc.)
                    Object parsedValue = new JSONObject("{\"value\":" + value + "}")
                            .get("value");
                    jsonObject.append(name, parsedValue);
                    continue;

                } catch (JSONException e) {
                    // String-parsing failed so fall back
                    jsonObject.append(name, value);
                }
                continue;

            }

            // For string parameters (like name, description), always keep as string
            // This prevents numeric strings like "2025" from being converted to numbers
            jsonObject.append(name, value);
        }

        return jsonObject.toString();
    }

    /**
     * Executes the request and returns the FULL raw JSON response as a String.
     *
     * @return A {@link Mono} emitting the raw JSON response as a String.
     */
    @NotNull
    public Mono<String> raw() {
        return Mono.fromCallable(this::execute);
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

            Mono.just(executeAndParse())
                    .doOnError(actual::onError)
                    .subscribe(actual);

        } catch (Throwable e) {
            actual.onError(e);
        }
    }

    protected abstract String execute() throws GeniusApiException, IOException, ParseException;

    protected abstract T executeAndParse() throws GeniusApiException, IOException, ParseException;

    /**
     * Executes a POST request and returns the raw JSON response as a String.
     *
     * @return The raw JSON response as a String, or null if the response is empty.
     *
     * @throws GeniusApiException if an error occurs during the request.
     * @throws IOException        if an I/O error occurs.
     */
    @Nullable
    protected String executePOST() throws GeniusApiException, IOException {
        return returnOrNull(
                this.httpManager.post(
                        this.uri,
                        this.headers.toArray(Header[]::new),
                        this.getBody()
                )
        );
    }

    /**
     * Executes a GET request and returns the raw JSON response as a String.
     *
     * @return The raw JSON response as a String, or null if the response is empty.
     *
     * @throws GeniusApiException if an error occurs during the request.
     * @throws IOException        if an I/O error occurs.
     * @throws ParseException     if an error occurs while parsing the response.
     */
    @Nullable
    protected String executeGET() throws GeniusApiException, IOException, ParseException {
        return returnOrNull(
                this.httpManager.get(
                        this.uri,
                        this.headers.toArray(Header[]::new)
                )
        );
    }

    /**
     * Executes a PUT request and returns the raw JSON response as a String.
     *
     * @return The raw JSON response as a String, or null if the response is empty.
     *
     * @throws GeniusApiException if an error occurs during the request.
     * @throws IOException        if an I/O error occurs.
     */
    @Nullable
    protected String executePUT() throws GeniusApiException, IOException {
        return returnOrNull(
                this.httpManager.put(
                        this.uri,
                        this.headers.toArray(Header[]::new),
                        this.getBody()
                )
        );
    }

    /**
     * Executes a DELETE request and returns the raw JSON response as a String.
     *
     * @return The raw JSON response as a String, or null if the response is empty.
     *
     * @throws GeniusApiException if an error occurs during the request.
     * @throws IOException        if an I/O error occurs.
     */
    @Nullable
    protected String executeDELETE() throws GeniusApiException, IOException {
        return returnOrNull(
                this.httpManager.delete(
                        this.uri,
                        this.headers.toArray(Header[]::new)
                )
        );
    }

    /**
     * Returns the given JSON string or null if it is null or empty.
     *
     * @param json The JSON string to check.
     *
     * @return The original JSON string or null.
     */
    @Nullable
    private String returnOrNull(@Nullable String json) {
        if (json == null) {
            return null;
        }

        if (json.isEmpty()) {
            return null;
        }

        return json;
    }

    public static abstract class Builder<T, B extends Builder<T, ?>> {

        private final Set<ParameterEntry> pathParameters = new HashSet<>();
        private final Set<ParameterEntry> queryParameters = new HashSet<>();
        private final Set<ParameterEntry> bodyParameters = new HashSet<>();
        private final Set<HeaderEntry> headers = new LinkedHashSet<>();

        protected IHttpManager httpManager = GeniusClient.DEFAULT_HTTP_MANAGER;
        protected String scheme = GeniusClient.DEFAULT_REQUEST_SCHEME;
        protected String host = GeniusClient.DEFAULT_API_BASE;
        protected int port = GeniusClient.DEFAULT_REQUEST_PORT;

        protected ContentType contentType = null;
        protected String path = null;
        protected HttpEntity body = null;

        public Builder() {

        }

        protected List<NameValuePair> getPathParametersAsNameValuePairs() {
            return this.pathParameters
                    .stream()
                    .map(p -> new BasicNameValuePair(p.getName(), p.getValue()))
                    .collect(Collectors.toList());
        }

        protected List<NameValuePair> getBodyParametersAsNameValuePairs() {
            return this.bodyParameters
                    .stream()
                    .map(p -> new BasicNameValuePair(p.getName(), p.getValue()))
                    .collect(Collectors.toList());
        }

        protected List<NameValuePair> getQueryParametersAsNameValuePairs() {
            return this.queryParameters
                    .stream()
                    .map(p -> new BasicNameValuePair(p.getName(), p.getValue()))
                    .collect(Collectors.toList());
        }

        protected List<Header> getHeadersAsHeaderList() {
            return this.headers
                    .stream()
                    .map(h -> (Header) h)
                    .collect(Collectors.toList());
        }

        public B pathParameter(@NotNull String name, @NotNull String value) {
            this.pathParameters.add(
                    new ParameterEntry(name, value)
            );
            return instance();
        }

        public B queryParameter(@NotNull String name, @NotNull String value) {
            this.queryParameters.add(
                    new ParameterEntry(name, value)
            );
            return instance();
        }

        public B bodyParameter(@NotNull String name, @NotNull String value) {
            this.bodyParameters.add(
                    new ParameterEntry(name, value)
            );
            return instance();
        }

        public B header(@NotNull String name, @NotNull String value) {
            this.headers.add(
                    new HeaderEntry(name, value)
            );
            return instance();
        }

        public B httpManager(@NotNull IHttpManager httpManager) {
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

        public B body(@NotNull HttpEntity body) {
            this.body = body;
            return instance();
        }

        public abstract IRequest<T> build();

        protected abstract B instance();

    }

    /**
     * Parameter entry class for request parameters.
     * Used to prevent duplicate parameters in the request.
     */
    public static class ParameterEntry {
        private final String name;
        private final String value;

        public ParameterEntry(@NotNull String name, @NotNull String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            ParameterEntry parameter = (ParameterEntry) obj;
            return name.equals(parameter.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    /**
     * Header entry class for request headers.
     * Used to prevent duplicate headers in the request.
     */
    public static class HeaderEntry implements Header {
        private final String name;
        private final String value;

        public HeaderEntry(@NotNull String name, @NotNull String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public boolean isSensitive() {
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            HeaderEntry header = (HeaderEntry) obj;
            return name.equals(header.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

}
