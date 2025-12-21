package dev.spoocy.genius.core.http.u_requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.data.sections.SectionsObject;
import dev.spoocy.genius.core.http.GeniusApiRequest;
import dev.spoocy.genius.core.http.SearchType;
import dev.spoocy.genius.core.http.requests.SearchRequest;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.genius.model.Sections;
import dev.spoocy.utils.common.misc.Args;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SectionsSearchRequest extends GeniusApiRequest<Sections> {

    protected SectionsSearchRequest(@NotNull Builder builder) {
        super(builder);
    }

    @Override
    protected String execute() throws GeniusApiException, IOException, ParseException {
        return this.executeGET();
    }

    @Override
    protected Sections executeAndParse() throws GeniusApiException, IOException, ParseException {
        return SectionsObject.Parser.INSTANCE.parseResponse(execute());
    }

    public static class Builder
            extends GeniusApiRequest.Builder<Sections, Builder> {

        public Builder(@NotNull GeniusClient client) {
            super(client);
            this.path("/api/search/{type}");
        }

        public Builder type(@NotNull SearchType type) {
            Args.notNull(type, "type");
            return this.pathParameter("type", type.getApiPath());
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
         * Specifies the Number of results to return per request. Default is 20.
         *
         * @param perPage the Number of results to return
         *
         * @return Builder instance
         */
        public Builder per_page(final int perPage) {
            Args.positive(perPage, "perPage");
            return this.queryParameter("per_page", String.valueOf(perPage));
        }

        /**
         * Specifies the Page number of the results to return. Default is 1.
         *
         * @param page the Page number of the results to return
         *
         * @return Builder instance
         */
        public Builder page(final int page) {
            Args.positive(page, "page");
            return this.queryParameter("page", String.valueOf(page));
        }

        /**
         * Builds the {@link SearchRequest} instance.
         *
         * @return The built Request
         */
        @Override
        public SectionsSearchRequest build() {
            return new SectionsSearchRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }


    }

}
