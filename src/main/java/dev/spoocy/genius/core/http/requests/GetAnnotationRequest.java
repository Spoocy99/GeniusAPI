package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.http.GeniusApiRequest;
import dev.spoocy.genius.core.data.annotations.AnnotationDataObject;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.genius.model.AnnotationData;
import dev.spoocy.utils.common.misc.Args;
import dev.spoocy.utils.common.misc.NumberConversion;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Data for a specific annotation.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GetAnnotationRequest extends GeniusApiRequest<AnnotationData> implements FormatableRequest {

    private final TextFormat[] format;

    protected GetAnnotationRequest(@NotNull Builder builder) {
        super(builder);

        this.format = builder.format;
    }

    @Override
    protected String execute() throws GeniusApiException, IOException, ParseException {
        return this.executeGET();
    }

    @Override
    protected AnnotationData executeAndParse() throws GeniusApiException, IOException, ParseException {
        return AnnotationDataObject.Parser.INSTANCE.parseResponse(execute());
    }

    @Override
    public TextFormat[] getFormats() {
        return this.format;
    }

    public static class Builder
            extends GeniusApiRequest.Builder<AnnotationData, Builder>
            implements FormatableRequest.Builder<Builder> {

        private TextFormat[] format = new TextFormat[0];

        public Builder(@NotNull GeniusClient client) {
            super(client);
            this.path("/annotations/{id}");
        }

        /**
         * Specifies the ID of the annotation to retrieve.
         *
         * @param id the ID of the annotation
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
         * Builds the {@link GetAnnotationRequest} instance.
         *
         * @return The built Request
         */
        @Override
        public GetAnnotationRequest build() {
            return new GetAnnotationRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }

    }


}
