package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.http.GeniusApiRequest;
import dev.spoocy.genius.core.data.webpages.WebPageObject;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.genius.model.WebPage;
import dev.spoocy.utils.common.misc.Args;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Information about a web page retrieved by the page’s full URL (including protocol).
 * The returned data includes Genius’s ID for the page, which may be used to look up associated
 * referents with the /referents endpoint.
 * <p>
 * Data is only available for pages that already have at least one annotation.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GetWebPagesLookupRequest extends GeniusApiRequest<WebPage> {

    protected GetWebPagesLookupRequest(@NotNull Builder builder) {
        super(builder);
    }

    @Override
    protected String execute() throws GeniusApiException, IOException, ParseException {
        return this.executeGET();
    }

    @Override
    protected WebPage executeAndParse() throws GeniusApiException, IOException, ParseException {
        return WebPageObject.Parser.INSTANCE.parseResponse(execute());
    }

    public static class Builder
            extends GeniusApiRequest.Builder<WebPage, Builder> {

        public Builder(@NotNull GeniusClient client) {
            super(client);
            this.path("/web_pages/lookup");
        }

        /**
         * Specifies the URL of the web page to look up.
         * <p>
         * e.g., https://docs.genius.com
         *
         * @param url the URL as it would appear in a browser
         *
         * @return Builder instance
         */
        public Builder raw_annotatable_url(@NotNull String url) {
            Args.notNullOrEmpty(url, "raw_annotatable_url");
            return this.queryParameter("raw_annotatable_url", url);
        }

        /**
         * Specifies the URL as indicated by a canonical <link> tag in a page's <head>.
         *
         * @param url the URL as specified by an appropriate <link> tag in a page's <head>
         *
         * @return Builder instance
         */
        public Builder canonical_url(@NotNull String url) {
            Args.notNullOrEmpty(url, "canonical_url\"");
            return this.queryParameter("canonical_url", url);
        }

        /**
         * Specifies the URL as indicated by an og:url <meta> tag in a page's <head>.
         *
         * @param url the URL as specified by an og:url <meta> tag in a page's <head>.
         *
         * @return Builder instance
         */
        public Builder og_url(@NotNull String url) {
            Args.notNullOrEmpty(url, "og_url");
            return this.queryParameter("og_url", url);
        }

        /**
         * Builds the {@link GetWebPagesLookupRequest} instance.
         *
         * @return The built Request
         */
        @Override
        public GetWebPagesLookupRequest build() {
            return new GetWebPagesLookupRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }

    }

}
