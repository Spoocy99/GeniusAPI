package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.Sort;
import dev.spoocy.genius.core.data.artists.ArtistSongsObject;
import dev.spoocy.genius.core.http.GeniusApiRequest;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.genius.model.ArtistSongs;
import dev.spoocy.utils.common.misc.Args;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Documents (songs) for the artist specified. By default, 20 items are returned for each request.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GetArtistSongsRequest extends GeniusApiRequest<ArtistSongs> {

    protected GetArtistSongsRequest(@NotNull Builder builder) {
        super(builder);
    }

    @Override
    protected String execute() throws GeniusApiException, IOException, ParseException {
        return this.executeGET();
    }

    @Override
    protected ArtistSongs executeAndParse() throws GeniusApiException, IOException, ParseException {
        return ArtistSongsObject.Parser.INSTANCE.parseResponse(execute());
    }

    public static class Builder
            extends GeniusApiRequest.Builder<ArtistSongs, Builder> {

        public Builder(@NotNull GeniusClient client) {
            super(client);
            this.path("/artists/{id}/songs");
        }

        /**
         * Specifies the ID of the artist.
         *
         * @param id the ID of the song to retrieve.
         *
         * @return Builder instance
         */
        public Builder id(long id) {
            Args.notNegative(id, "id");
            return this.pathParameter("id", String.valueOf(id));
        }

        /**
         * Specifies the sort order of the results.
         *
         * @param sort title (default) or popularity
         *
         * @return Builder instance
         */
        public Builder sort(@NotNull Sort sort) {
            Args.notNull(sort, "sort");
            return this.queryParameter("sort", sort.getKey());
        }

        /**
         * Specifies the number of results to return.
         * <p>
         * e.g., per_page=5 & page=3 returns songs 11–15
         *
         * @param perPage the Number of results to return
         *
         * @return Builder instance
         */
        public Builder per_page(int perPage) {
            Args.positive(perPage, "perPage");
            return this.queryParameter("per_page", String.valueOf(perPage));
        }

        /**
         * Specifies the paginated offset.
         * <p>
         * e.g., per_page=5 & page=3 returns songs 11–15
         *
         * @param page the Paginated offset
         *
         * @return Builder instance
         */
        public Builder page(int page) {
            Args.positive(page, "page");
            return this.queryParameter("page", String.valueOf(page));
        }

        /**
         * Builds the {@link GetArtistSongsRequest} instance.
         *
         * @return The built Request
         */
        @Override
        public GetArtistSongsRequest build() {
            return new GetArtistSongsRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }

    }

}
