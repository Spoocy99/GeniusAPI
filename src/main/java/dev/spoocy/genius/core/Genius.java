package dev.spoocy.genius.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class Genius {

    /**
     * Indicates that the annotated request is not part of Genius' API documentation and
     * might behave unexpectedly or stop working at any time.
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Undocumented { }

    /**
     * Indicates that the annotated request is not part of Genius' official API and
     * might behave unexpectedly or stop working at any time.
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Unofficial { }

    private Genius() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}
