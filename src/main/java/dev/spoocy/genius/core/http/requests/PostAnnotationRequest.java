package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.http.GeniusApiRequest;
import dev.spoocy.genius.exception.GeniusApiException;
import dev.spoocy.genius.model.AnnotationData;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Data for a specific annotation.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class PostAnnotationRequest extends GeniusApiRequest<AnnotationData> {

    protected PostAnnotationRequest(@NotNull Builder builder) {
        super(builder);
    }

    @Override
    protected @NotNull Mono<String> execute0() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    protected @NotNull AnnotationData parseResponse(@NotNull String response) throws GeniusApiException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static class Builder
            extends GeniusApiRequest.Builder<AnnotationData, Builder> {

        public Builder(@NotNull GeniusClient client) {
            super(client);
            this.path("/annotations");
        }

        /**
         * Builds the {@link PostAnnotationRequest} instance.
         *
         * @return The built Request
         */
        @Override
        public PostAnnotationRequest build() {
            return new PostAnnotationRequest(this);
        }

        @Override
        protected Builder instance() {
            return this;
        }

    }


}
