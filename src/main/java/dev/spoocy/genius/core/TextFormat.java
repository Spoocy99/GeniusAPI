package dev.spoocy.genius.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
@AllArgsConstructor
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

    private final String format;
}
