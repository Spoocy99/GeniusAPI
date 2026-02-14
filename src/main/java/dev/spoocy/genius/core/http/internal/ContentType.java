package dev.spoocy.genius.core.http.internal;

import java.util.Objects;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ContentType {
    public static final ContentType APPLICATION_JSON = new ContentType("application/json");
    public static final ContentType APPLICATION_FORM_URLENCODED = new ContentType("application/x-www-form-urlencoded");

    private final String type;

    public ContentType(String type) {
        this.type = type;
    }

    public String getMimeType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ContentType)) return false;
        ContentType that = (ContentType) o;
        return Objects.equals(type, that.type);
    }
}
