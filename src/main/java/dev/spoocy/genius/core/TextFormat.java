package dev.spoocy.genius.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public enum TextFormat {
    /*
    * plain is just plain text, no markup
    */
    PLAIN("plain"),

    /*
    * html is a string of unescaped HTML suitable
    * for rendering by a browser
    */
    HTML("html");

    private final String key;

    TextFormat(@NotNull String key) {
        this.key = key;
    }

    @NotNull
    public String getKey() {
        return this.key;
    }

    public static String format(@NotNull TextFormat[] formats) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < formats.length; i++) {
            sb.append(formats[i].getKey());
            if (i < formats.length - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

}
