package dev.spoocy.genius.core.http;

import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.core5.concurrent.CancellableDependency;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.Args;
import org.apache.hc.core5.util.TimeValue;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GeniusHttpRequestRetryStrategy implements HttpRequestRetryStrategy {

    private final int maxRetries;
    private final TimeValue defaultRetryInterval;
    private final Set<Integer> retryCodes;

    protected GeniusHttpRequestRetryStrategy(
            int maxRetries,
            @NotNull TimeValue defaultRetryInterval,
            @NotNull Collection<Integer> retryCodes
    ) {

        this.maxRetries = Args.positive(maxRetries, "Max retries");
        this.defaultRetryInterval = Args.notNull(defaultRetryInterval, "Default retry interval");
        this.retryCodes = new HashSet<>(retryCodes);
    }

    @Override
    public boolean retryRequest(HttpRequest request, IOException exception, int execCount, HttpContext context) {

        if (execCount > this.maxRetries) {
            // max retry count reached
            return false;
        }

        if (request instanceof CancellableDependency && ((CancellableDependency) request).isCancelled()) {
            // request has been cancelled
            return false;
        }

        // retry if the request is considered idempotent
        return Method.isIdempotent(request.getMethod());
    }

    @Override
    public boolean retryRequest(HttpResponse response, int execCount, HttpContext context) {
        return execCount <= this.maxRetries && this.retryCodes.contains(response.getCode());
    }

    @Override
    public TimeValue getRetryInterval(HttpResponse response, int execCount, HttpContext context) {
        return this.defaultRetryInterval;
    }

}
