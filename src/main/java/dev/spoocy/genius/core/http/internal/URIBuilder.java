package dev.spoocy.genius.core.http.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Minimal URI builder.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public final class URIBuilder {

    private String scheme;
    private String host;
    private int port = -1;
    private String path = "";
    private List<Parameter> parameters = null;

    public URIBuilder scheme(@NotNull String scheme) {
        this.scheme = scheme;
        return this;
    }

    public URIBuilder host(@NotNull String host) {
        this.host = host;
        return this;
    }

    public URIBuilder port(int port) {
        this.port = port;
        return this;
    }

    public URIBuilder path(@NotNull String path) {
        this.path = path;
        return this;
    }

    public URIBuilder path(@NotNull String path, @NotNull List<Parameter> parameters) {
        for (Parameter par : parameters) {
            String key = "\\{" + par.getName() + "\\}";
            String value = par.getValue();
            path = path.replaceAll(key, value);
        }

        this.path = path;
        return this;
    }

    public URIBuilder parameters(@Nullable List<Parameter> parameters) {
        this.parameters = parameters;
        return this;
    }

    public URI build() throws URISyntaxException {
        Objects.requireNonNull(scheme, "Scheme");
        Objects.requireNonNull(host, "Host");

        String normalizedPath = normalizePath(this.path);
        String query = buildQuery(this.parameters);

        URI uri;
        if (this.port >= 0) {
            uri = new URI(this.scheme, null, this.host, this.port, normalizedPath, query, null);
        } else {
            uri = new URI(this.scheme, null, this.host, -1, normalizedPath, query, null);
        }

        return uri;
    }

    private static String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        // Ensure path starts with a single '/'
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }

    private static String buildQuery(@Nullable List<Parameter> params) {
        if (params == null || params.isEmpty()) return null;

        StringJoiner joiner = new StringJoiner("&");

        for (Parameter p : params) {
            String name = p.getName() == null ? "" : p.getName();
            String value = p.getValue() == null ? "" : p.getValue();

            try {
                String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
                joiner.add(name + "=" + value);
            } catch (Exception e) {
                // Should not happen; fallback to raw values
                joiner.add(name + "=" + value);
            }
        }

        return joiner.toString();
    }

}
