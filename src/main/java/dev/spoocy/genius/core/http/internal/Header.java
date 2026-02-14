package dev.spoocy.genius.core.http.internal;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class Header {

    private final String name;
    private final String value;
    private final boolean sensitive;

    public Header(@NotNull String name, @NotNull String value) {
        this(name, value, false);
    }

    public Header(@NotNull String name, @NotNull String value, boolean isSensitive) {
        this.name = name;
        this.value = value;
        this.sensitive = isSensitive;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isSensitive() {
        return this.sensitive;
    }

    @Override
    public String toString() {
        return name + ": " + (sensitive ? "[REDACTED]" : value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Header header = (Header) obj;
        return name.equals(header.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
