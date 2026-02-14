package dev.spoocy.genius.core.http;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import reactor.netty.http.client.HttpClientResponse;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface NettyResponseHandler<T> {

    Mono<T> handle(@NotNull HttpClientResponse response, @NotNull ByteBufMono byteBufMono);

}
