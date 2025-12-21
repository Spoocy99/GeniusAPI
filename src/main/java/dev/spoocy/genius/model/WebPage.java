package dev.spoocy.genius.model;

import dev.spoocy.genius.core.data.GeniusApiDataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A web page is a single, publicly accessible page to which annotations
 * may be attached. Web pages map 1-to-1 with unique, canonical URLs.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface WebPage extends GeniusApiDataObject {

    @NotNull
    String getApiPath();

    @NotNull
    String getDomain();

    @NotNull
    Long getId();

    @NotNull
    String getNormalizedUrl();

    @NotNull
    String getShareUrl();

    @NotNull
    String getTitle();

    @Nullable
    String getUrl();

    @Nullable
    Integer getAnnotationCount();

}
