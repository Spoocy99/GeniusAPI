package dev.spoocy.genius.core.http.internal;

import dev.spoocy.utils.common.misc.Args;
import org.jetbrains.annotations.NotNull;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GeniusHttpRequestRetryStrategy {

    private final int maxRetries;
    private final Duration defaultRetryInterval;
    private final Set<Class<? extends IOException>> nonRetriableExceptions;
    private final Retry reactor;

    protected GeniusHttpRequestRetryStrategy(
            int maxRetries,
            @NotNull Duration defaultRetryInterval,
            @NotNull Collection<Class<? extends IOException>> nonRetriableIOExceptions
    ) {

        this.maxRetries = Args.positive(maxRetries, "Max retries");
        this.defaultRetryInterval = Args.notNull(defaultRetryInterval, "Default retry interval");
        this.nonRetriableExceptions = new HashSet<>(nonRetriableIOExceptions);

        // Retry on IOExceptions (including wrapped ones). Do not retry if the
        // root cause is a non-retriable IOException type (or subtype).
        this.reactor = Retry
                .fixedDelay(this.maxRetries, this.defaultRetryInterval)
                .filter(throwable -> {
                    // Unwrap cause chain to find an IOException root cause
                    Throwable t = throwable;
                    while (t != null && !(t instanceof IOException)) {
                        t = t.getCause();
                    }

                    if (t == null) {
                        return false; // no IOException root cause -> don't retry
                    }

                    // If any configured non-retriable exception class is assignable from
                    // the root cause class, treat it as non-retriable.
                    Class<?> causeClass = t.getClass();
                    for (Class<? extends IOException> nonRetriable : this.nonRetriableExceptions) {
                        if (nonRetriable.isAssignableFrom(causeClass)) {
                            return false; // found a matching non-retriable type
                        }
                    }

                    return true;
                });
    }

    public int maxRetries() {
        return this.maxRetries;
    }

    public Duration defaultRetryInterval() {
        return this.defaultRetryInterval;
    }

    public Set<Class<? extends IOException>> nonRetriableExceptions() {
        return this.nonRetriableExceptions;
    }

    public Retry reactor() {
        return this.reactor;
    }

}
