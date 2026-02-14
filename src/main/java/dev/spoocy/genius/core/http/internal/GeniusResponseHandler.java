package dev.spoocy.genius.core.http.internal;

import dev.spoocy.genius.core.http.NettyResponseHandler;
import dev.spoocy.genius.exception.*;
import dev.spoocy.utils.common.log.ILogger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import reactor.netty.http.client.HttpClientResponse;

import java.io.IOException;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GeniusResponseHandler implements NettyResponseHandler<String> {

    private static final ILogger LOGGER = ILogger.forClass(GeniusResponseHandler.class);

    @Override
    public Mono<String> handle(@NotNull HttpClientResponse response, @NotNull ByteBufMono byteBufMono) {
        Mono<String> bodyMono = byteBufMono.asString();

        return bodyMono.flatMap(responseBody -> {
            String errorMessage = response.status().reasonPhrase();

            String responsePreview = responseBody == null ? "" : responseBody;
            if (responsePreview.length() > 1024) {
                responsePreview = responsePreview.substring(0, 1024) + "...";
            }

            LOGGER.trace("Http Response (Code {}, Message: {}): {}\nResponse Body: {}",
                    response.status().code(),
                    response.status().reasonPhrase(),
                    (responseBody != null && !responseBody.isEmpty()) ? "Received Response" : "Empty Response",
                    responsePreview
            );

            if (responseBody != null && responseBody.startsWith("{")) {
                try {
                    JSONObject json = new JSONObject(responseBody);

                    if (json.has("meta")) {
                        JSONObject meta = json.getJSONObject("meta");

                        if (meta.has("message")) {
                            errorMessage = meta.getString("message");
                        }
                    }

                } catch (Exception ignored) {
                    // no json body to extract error message from
                }
            }

            int statusCode = response.status().code();

            switch (statusCode) {
                case 400:
                    return Mono.error(new BadRequestException(errorMessage));
                case 403:
                    return Mono.error(new ForbiddenException(errorMessage));
                case 429:
                    return Mono.error(new RateLimitException(errorMessage));
                default:
                    break;
            }

            if (statusCode >= 400 && statusCode < 500) {
                return Mono.error(new BadRequestException(errorMessage));

            } else if (statusCode >= 500) {
                return Mono.error(new InternalServerErrorException(errorMessage));
            }

            return Mono.justOrEmpty(responseBody);
        }).onErrorMap(throwable -> {

            if (throwable instanceof GeniusApiException) {
                return throwable;
            }

            if (throwable instanceof IOException) {
                return new GeniusApiException(throwable.getMessage(), throwable);
            }

            return throwable;
        });
    }
}
