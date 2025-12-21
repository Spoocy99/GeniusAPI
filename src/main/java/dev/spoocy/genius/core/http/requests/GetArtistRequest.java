package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.http.GeniusApiRequest;
import dev.spoocy.genius.core.data.artists.ArtistObject;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.genius.model.Artist;
import dev.spoocy.utils.common.misc.Args;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Data for a specific artist.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GetArtistRequest extends GeniusApiRequest<Artist> implements FormatableRequest {

    protected final TextFormat[] format;

    protected GetArtistRequest(@NotNull Builder builder) {
        super(builder);
        this.format = builder.format;
    }

    @Override
    public TextFormat[] getFormats() {
        return this.format;
    }

    @Override
    protected String execute() throws GeniusApiException, IOException, ParseException {
        return this.executeGET();
    }

    @Override
    protected Artist executeAndParse() throws GeniusApiException, IOException, ParseException {
        return ArtistObject.Parser.INSTANCE.parseResponse(execute());
    }

    public static class Builder
            extends GeniusApiRequest.Builder<Artist, Builder>
            implements FormatableRequest.Builder<Builder> {

        protected TextFormat[] format = new TextFormat[0];

        public Builder(@NotNull GeniusClient client) {
            super(client);
            this.path("/artists/{id}");
        }

        /**
         * Specifies the ID of the artist to retrieve.
         *
         * @param id the ID of the artist
         *
         * @return Builder instance
         */
        public Builder id(@NotNull String id) {
            Args.notNullOrEmpty(id, "id");
            return this.pathParameter("id", id);
        }

        /**
         * Specifies the format for text bodies related to the document.
         * <br> Only the provided {@link TextFormat} values will be included in the response.
         *
         * @param format format for text bodies related to the document
         *
         * @return Builder instance
         */
        @Override
        public Builder formats(@NotNull TextFormat... format) {
            this.format = format;
            if (format.length > 0) {
                this.queryParameter("text_format", TextFormat.format(format));
            }
            return this;
        }

        /**
         * Builds the {@link GetArtistRequest} instance.
         *
         * @return The built Request
         */
        @Override
        public GetArtistRequest build() {
            return new GetArtistRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }

    }

}
