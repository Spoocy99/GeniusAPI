package dev.spoocy.genius.core.data.webpages;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.model.Song;
import dev.spoocy.genius.model.WebPage;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * Implementation for {@link WebPage}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public class WebPageObject implements WebPage {

    @NotNull
    private final String apiPath;

    @NotNull
    private final String domain;

    @NotNull
    private final Long id;

    @NotNull
    private final String normalizedUrl;

    @NotNull
    private final String shareUrl;

    @NotNull
    private final String title;

    @Nullable
    private final String url;

    @Nullable
    private final Integer annotationCount;

    public WebPageObject(
            @NotNull String apiPath,
            @NotNull String domain,
            @NotNull Long id,
            @NotNull String normalizedUrl,
            @NotNull String shareUrl,
            @NotNull String title,
            @Nullable String url,
            @Nullable Integer annotationCount
    ) {
        this.apiPath = apiPath;
        this.domain = domain;
        this.id = id;
        this.normalizedUrl = normalizedUrl;
        this.shareUrl = shareUrl;
        this.title = title;
        this.url = url;
        this.annotationCount = annotationCount;
    }

    @Override
    public @NotNull String getApiPath() { return this.apiPath; }

    @Override
    public @NotNull String getDomain() { return this.domain; }

    @Override
    public @NotNull Long getId() { return this.id; }

    @Override
    public @NotNull String getNormalizedUrl() { return this.normalizedUrl; }

    @Override
    public @NotNull String getShareUrl() { return this.shareUrl; }

    @Override
    public @NotNull String getTitle() { return this.title; }

    @Override
    public @Nullable String getUrl() { return this.url; }

    @Override
    public @Nullable Integer getAnnotationCount() { return this.annotationCount; }

    public static final class Parser extends AbstractModelParser<WebPage> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {}

        @Override
        public WebPage parseResponse(String json) {
            return this.parse(
                new JSONObject(json)
                        .getJSONObject("response")
                        .getJSONObject("web_page")
        );
        }

        @Override
        public WebPage parse0(@NotNull Config data) {
            return new WebPageObject(
                    data.getString("api_path", ""),
                    data.getString("domain", ""),
                    data.getLong("id", 0L),
                    data.getString("normalized_url", ""),
                    data.getString("share_url", ""),
                    data.getString("title", ""),
                    data.getString("url", ""),
                    integerOrNull(data, "annotation_count")
            );
        }
    }

}

