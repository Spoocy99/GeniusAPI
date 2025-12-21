package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.http.GeniusApiRequest;
import dev.spoocy.genius.core.data.account.AccountObject;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.genius.model.Account;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Data for a specific artist.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GetAccountRequest extends GeniusApiRequest<Account> implements FormatableRequest {

    private final TextFormat[] format;

    protected GetAccountRequest(@NotNull Builder builder) {
        super(builder);

        this.format = builder.format;
    }

    @Override
    protected String execute() throws GeniusApiException, IOException, ParseException {
        return this.executeGET();
    }

    @Override
    protected Account executeAndParse() throws GeniusApiException, IOException, ParseException {
        return AccountObject.Parser.INSTANCE.parseResponse(execute());
    }

    @Override
    public TextFormat[] getFormats() {
        return this.format;
    }

    public static class Builder
            extends GeniusApiRequest.Builder<Account, Builder>
            implements FormatableRequest.Builder<Builder> {

        private TextFormat[] format = new TextFormat[0];

        public Builder(@NotNull GeniusClient client) {
            super(client);
            this.path("/account");
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
         * Builds the {@link GetAccountRequest} instance.
         *
         * @return The built Request
         */
        @Override
        public GetAccountRequest build() {
            return new GetAccountRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }

    }


}
