package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.data.songs.SongObject;
import dev.spoocy.genius.core.http.GeniusApiRequest;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.genius.model.Song;
import dev.spoocy.utils.common.misc.Args;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Data for a specific song.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GetSongRequest extends GeniusApiRequest<Song> implements FormatableRequest {

    protected final TextFormat[] format;

    protected GetSongRequest(@NotNull Builder builder) {
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
    protected Song executeAndParse() throws GeniusApiException, IOException, ParseException {
        return SongObject.Parser.INSTANCE.parseResponse(this.execute());
    }

    public static class Builder
            extends GeniusApiRequest.Builder<Song, Builder>
            implements FormatableRequest.Builder<Builder> {

        protected TextFormat[] format = new TextFormat[0];

        public Builder(@NotNull GeniusClient client) {
            super(client);
            this.path("/songs/{id}");
        }

        /**
         * Specifies the ID of the song to retrieve.
         *
         * @param id the ID of the song to retrieve
         *
         * @return Builder instance
         */
        public Builder id(long id) {
            Args.notNegative(id, "id");
            return this.pathParameter("id", String.valueOf(id));
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
         * Builds the {@link GetSongRequest} instance.
         *
         * @return The built Request
         */
        @Override
        public GetSongRequest build() {
            return new GetSongRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }

    }

}
