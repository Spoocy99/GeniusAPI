package dev.spoocy.genius.data;

import dev.spoocy.common.config.Config;
import lombok.Getter;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
public class Stats {

    private final int acceptedAnnotations;
    private final int contributors;
    private final int iqEarners;
    private final int transcribers;
    private final int unreviewedAnnotations;
    private final int verifiedAnnotations;
    private final boolean hot;
    private final int pageviews;

    public Stats(Config data) {
        this.acceptedAnnotations = data.getInt("accepted_annotations", 0);
        this.contributors = data.getInt("contributors", 0);
        this.iqEarners = data.getInt("iq_earners", 0);
        this.transcribers = data.getInt("transcribers", 0);
        this.unreviewedAnnotations = data.getInt("unreviewed_annotations", 0);
        this.verifiedAnnotations = data.getInt("verified_annotations", 0);
        this.hot = data.getBoolean("hot", false);
        this.pageviews = data.getInt("pageviews", 0);
    }

    @Override
    public String toString() {
        return "Stats{" +
                "acceptedAnnotations=" + acceptedAnnotations +
                ", contributors=" + contributors +
                ", iqEarners=" + iqEarners +
                ", transcribers=" + transcribers +
                ", unreviewedAnnotations=" + unreviewedAnnotations +
                ", verifiedAnnotations=" + verifiedAnnotations +
                ", hot=" + hot +
                ", pageviews=" + pageviews +
                '}';
    }
}
