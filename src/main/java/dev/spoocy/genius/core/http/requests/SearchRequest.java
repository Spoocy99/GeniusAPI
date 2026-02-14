package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.http.GeniusApiRequest;
import dev.spoocy.genius.core.data.search.SearchObject;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.genius.model.Search;
import dev.spoocy.utils.common.misc.Args;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Information about a web page retrieved by the page’s full URL (including protocol).
 * The returned data includes Genius’s ID for the page, which may be used to look up associated
 * referents with the /referents endpoint.
 * <p>
 * Data is only available for pages that already have at least one annotation.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SearchRequest extends GeniusApiRequest<Search> {

    protected SearchRequest(@NotNull Builder builder) {
        super(builder);
    }

    @Override
    protected @NotNull Mono<String> execute0() {
        return this.executeGET();
    }

    @Override
    protected @NotNull Search parseResponse(@NotNull String response) throws GeniusApiException {
        return SearchObject.Parser.INSTANCE.parseResponse(response);
    }

    public static class Builder
            extends GeniusApiRequest.Builder<Search, Builder> {

        public Builder(@NotNull GeniusClient client) {
            super(client);
            this.path("/search");
        }

        /**
         * Specifies the search query.
         *
         * @param search the term to search for
         *
         * @return Builder instance
         */
        public Builder query(@NotNull String search) {
            Args.notNullOrEmpty(search, "query");
            return this.queryParameter("q", search);
        }

        /**
         * Builds the {@link SearchRequest} instance.
         *
         * @return The built Request
         */
        @Override
        public SearchRequest build() {
            return new SearchRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }

    }

}
