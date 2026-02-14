package dev.spoocy.genius.core.http.internal;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpHeaders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONParserConfiguration;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class NettyEntity {

    private static final Header DEFAULT_CONTENT_ENCODING = new Header("Content-Encoding", "utf-8");
    private static final Set<String> KEEP_DATA_TYPES = Set.of(
            "grant_type", "response_type", "redirect_uri",
            "client_id", "client_secret", "code"
    );

    public static NettyEntity fromString(@NotNull String json, @NotNull ContentType contentType) {
        return new StringNettyEntity(json, contentType);
    }

    public static NettyEntity fromFormUrlEncoded(@NotNull Collection<Parameter> parameters) {
        return new FormUrlEncodedNettyEntity(parameters);
    }

    public static NettyEntity jsonParameters(@NotNull Collection<Parameter> bodyParameters) {
        String json = bodyParametersToJson(bodyParameters);
        return fromString(json, ContentType.APPLICATION_JSON);
    }

    /**
     * Converts body parameters to a JSON string, preserving data types for specific parameters.
     *
     * @param bodyParameters The list of body parameters as NameValuePair.
     *
     * @return A JSON string representation of the body parameters.
     */
    private static String bodyParametersToJson(@NotNull Collection<Parameter> bodyParameters) {
        JSONObject jsonObject = new JSONObject();

        for (Parameter nameValuePair : bodyParameters) {
            String name = nameValuePair.getName();
            String value = nameValuePair.getValue();

            if (KEEP_DATA_TYPES.contains(name)) {

                try {

                    // Try to parse the value as JSON
                    Object parsedValue = new JSONObject(value, new JSONParserConfiguration());
                    jsonObject.put(name, parsedValue);
                    continue;

                } catch (JSONException ignored) {  }

            }

            // Use put here as well so values are plain fields in the JSON object
            jsonObject.put(name, value);
        }

        return jsonObject.toString();
    }

    protected final ContentType contentType;
    protected final Long contentLength;
    protected final Header contentEncoding;

    private NettyEntity(
            @NotNull ContentType contentType,
            @Nullable Long contentLength,
            @NotNull Header contentEncoding
    ) {
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.contentEncoding = contentEncoding;

    }

    @NotNull
    public ContentType getContentType() {
        return this.contentType;
    }

    @Nullable
    public Long getContentLength() {
        return this.contentLength;
    }

    @NotNull
    public Header getContentEncoding() {
        return this.contentEncoding;
    }

    private Publisher<? extends ByteBuf> body;
    @NotNull
    public Publisher<? extends ByteBuf> getBody() {
        if (this.body == null) {
            this.body = initBody();
        }
        return this.body;
    }

    protected abstract Publisher<? extends ByteBuf> initBody();

    public void applyHaders(@NotNull HttpHeaders headersBuilder) {
        headersBuilder.add("Content-Type", this.getContentType()
                .getMimeType());

        if (this.getContentLength() != null) {
            headersBuilder.add("Content-Length", String.valueOf(this.getContentLength()));
        }

        headersBuilder.add(this.getContentEncoding()
                .getName(), this.getContentEncoding()
                .getValue());
    }

    @Override
    public String toString() {
        return "NettyEntity{" +
                "contentType=" + contentType +
                ", contentLength=" + contentLength +
                ", contentEncoding=" + contentEncoding +
                '}';
    }

    public static class StringNettyEntity extends NettyEntity {

        private final String body;

        public StringNettyEntity(@NotNull String body, @NotNull ContentType contentType) {
            super(contentType, (long) body.getBytes(StandardCharsets.UTF_8).length, DEFAULT_CONTENT_ENCODING);
            this.body = body;
        }

        @Override
        protected Publisher<? extends ByteBuf> initBody() {
            ByteBuf byteBuf = Unpooled.copiedBuffer(this.body, StandardCharsets.UTF_8);
            return Mono.just(byteBuf);
        }

        @Override
        public String toString() {
            return "StringNettyEntity{" +
                    "contentType=" + contentType +
                    ", contentLength=" + contentLength +
                    ", contentEncoding=" + contentEncoding +
                    ", body='" + body + '\'' +
                    '}';
        }
    }

    public static class FormUrlEncodedNettyEntity extends NettyEntity {

        private final Collection<Parameter> parameters;

        public FormUrlEncodedNettyEntity(@NotNull Collection<Parameter> parameters) {
            super(ContentType.APPLICATION_FORM_URLENCODED, null, DEFAULT_CONTENT_ENCODING);
            this.parameters = parameters;
        }

        @Override
        protected Publisher<? extends ByteBuf> initBody() {
            StringBuilder bodyBuilder = new StringBuilder();

            for (Parameter param : parameters) {
                if (bodyBuilder.length() > 0) {
                    bodyBuilder.append("&");
                }
                String name = param.getName() == null ? "" : param.getName();
                String value = param.getValue() == null ? "" : param.getValue();
                bodyBuilder.append(name).append("=").append(value);
            }

            String body = bodyBuilder.toString();
            ByteBuf byteBuf = Unpooled.copiedBuffer(body, StandardCharsets.UTF_8);
            return Mono.just(byteBuf);
        }

        @Override
        public String toString() {
            return "FormUrlEncodedNettyEntity{" +
                    "contentType=" + contentType +
                    ", contentLength=" + contentLength +
                    ", contentEncoding=" + contentEncoding +
                    ", parameters=" + parameters +
                    '}';
        }
    }
}
