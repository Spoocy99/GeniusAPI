package dev.spoocy.genius.core.data.sections;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.model.Sections;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class RangeObject implements Sections.Range {

    /**
     * Path: sections.hits.highlights.ranges.start
     */
    @NotNull
    private final Integer start;

    /**
     * Path: sections.hits.highlights.ranges.end
     */
    @NotNull
    private final Integer end;

    public RangeObject(@NotNull Integer start, @NotNull Integer end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public @NotNull Integer getStart() {
        return this.start;
    }

    @Override
    public @NotNull Integer getEnd() {
        return this.end;
    }

    public static class Parser extends AbstractModelParser<Sections.Range> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        protected Sections.Range parse0(@NotNull Config data) {
            return new RangeObject(
                    data.getInt("start"),
                    data.getInt("end")
            );
        }
    }

}
