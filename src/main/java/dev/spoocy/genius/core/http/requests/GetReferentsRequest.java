package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.http.GeniusApiRequest;
import dev.spoocy.genius.core.data.referents.ReferentsObject;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.genius.model.Referents;
import dev.spoocy.utils.common.misc.Args;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Referents by content item or user responsible for an included annotation.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GetReferentsRequest extends GeniusApiRequest<Referents> implements FormatableRequest {

    private final TextFormat[] format;

    protected GetReferentsRequest(@NotNull Builder builder) {
        super(builder);

        this.format = builder.format;
    }

    @Override
    protected String execute() throws GeniusApiException, IOException, ParseException {
        return this.executeGET();
    }

    @Override
    protected Referents executeAndParse() throws GeniusApiException, IOException, ParseException {
        return ReferentsObject.Parser.INSTANCE.parseResponse(execute());
    }

    @Override
    public TextFormat[] getFormats() {
        return this.format;
    }

    public static class Builder
            extends GeniusApiRequest.Builder<Referents, Builder>
            implements FormatableRequest.Builder<Builder> {

        private TextFormat[] format = new TextFormat[0];

        public Builder(@NotNull GeniusClient client) {
            super(client);
            this.path("/referents");
        }

        /**
         * Specifies the ID of a user who created the annotations to get referents for.
         *
         * @param id the ID of a user to get referents for
         *
         * @return Builder instance
         */
        public Builder created_by_id(final long id) {
            Args.notNegative(id, "created_by_id");
            return this.queryParameter("created_by_id", String.valueOf(id));
        }

        /**
         * Specifies the ID of a song to get referents for.
         * <p>
         * <b>You may pass only one of song_id and web_page_id, not both.</b><br>
         *
         * @param id the ID of a song to get referents for
         *
         * @return Builder instance
         *
         * @see #web_page_id(long)
         */
        public Builder song_id(long id) {
            Args.notNegative(id, "song_id");
            return this.queryParameter("song_id", String.valueOf(id));
        }

        /**
         * Specifies the ID of a web page to get referents for.
         * <p>
         * <b>You may pass only one of song_id and web_page_id, not both.</b><br>
         *
         * @param id the ID of a web page to get referents for
         *
         * @return Builder instance
         *
         * @see #song_id(long)
         */
        public Builder web_page_id(final long id) {
            Args.notNegative(id, "web_page_id");
            return this.queryParameter("web_page_id", String.valueOf(id));
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
         * Builds the {@link GetReferentsRequest} instance.
         *
         * @return The built Request
         */
        @Override
        public GetReferentsRequest build() {
            return new GetReferentsRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }

    }


}
