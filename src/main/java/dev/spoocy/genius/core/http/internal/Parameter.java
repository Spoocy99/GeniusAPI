package dev.spoocy.genius.core.http.internal;

import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class Parameter {

    private final String name;
    private final String value;

    public Parameter(@NotNull String name, @NotNull String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Parameter parameter = (Parameter) obj;
        return name.equals(parameter.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
