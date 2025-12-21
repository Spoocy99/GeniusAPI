package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.TextFormat;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface FormatableRequest {

    TextFormat[] getFormats();

    interface Builder<T> {
        T formats(@NotNull TextFormat... format);
    }

}
