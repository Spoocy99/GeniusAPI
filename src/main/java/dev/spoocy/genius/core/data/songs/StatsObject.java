package dev.spoocy.genius.core.data.songs;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.model.Song;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation for {@link Song.Stats}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class StatsObject implements Song.Stats {

    @Nullable
    private final Integer acceptedAnnotations;
    @Nullable
    private final Integer contributors;
    @Nullable
    private final Integer iqEarners;
    @Nullable
    private final Integer transcribers;
    @Nullable
    private final Integer unreviewedAnnotations;
    @Nullable
    private final Integer verifiedAnnotations;
    @Nullable
    private final Integer concurrents;
    @NotNull
    private final Boolean hot;
    @Nullable
    private final Integer pageviews;

    public StatsObject(
            @Nullable Integer acceptedAnnotations,
            @Nullable Integer contributors,
            @Nullable Integer iqEarners,
            @Nullable Integer transcribers,
            @Nullable Integer unreviewedAnnotations,
            @Nullable Integer verifiedAnnotations,
            @Nullable Integer concurrents,
            @NotNull Boolean hot,
            @Nullable Integer pageviews
    ) {
        this.acceptedAnnotations = acceptedAnnotations;
        this.contributors = contributors;
        this.iqEarners = iqEarners;
        this.transcribers = transcribers;
        this.unreviewedAnnotations = unreviewedAnnotations;
        this.verifiedAnnotations = verifiedAnnotations;
        this.concurrents = concurrents;
        this.hot = hot;
        this.pageviews = pageviews;
    }

    @Override
    public @Nullable Integer getAcceptedAnnotations() {
        return this.acceptedAnnotations;
    }

    @Override
    public @Nullable Integer getContributors() {
        return this.contributors;
    }

    @Override
    public @Nullable Integer getIqEarners() {
        return this.iqEarners;
    }

    @Override
    public @Nullable Integer getTranscribers() {
        return this.transcribers;
    }

    @Override
    public @Nullable Integer getUnreviewedAnnotations() {
        return this.unreviewedAnnotations;
    }

    @Override
    public @Nullable Integer getVerifiedAnnotations() {
        return this.verifiedAnnotations;
    }

    @Override
    public @Nullable Integer getConcurrents() {
        return this.concurrents;
    }

    @Override
    public @NotNull Boolean isHot() {
        return this.hot;
    }

    @Override
    public @Nullable Integer getPageviews() {
        return this.pageviews;
    }


    public static class Parser extends AbstractModelParser<Song.Stats> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }


        @Override
        public Song.Stats parse0(@NotNull Config data) {
            return new StatsObject(
                    integerOrNull(data, "accepted_annotations"),
                    integerOrNull(data, "contributors"),
                    integerOrNull(data, "iq_earners"),
                    integerOrNull(data, "transcribers"),
                    integerOrNull(data, "unreviewed_annotations"),
                    integerOrNull(data, "verified_annotations"),
                    integerOrNull(data, "concurrents"),
                    data.getBoolean("hot", false),
                    integerOrNull(data, "pageviews")
            );
        }
    }

}
