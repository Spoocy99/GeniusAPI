package dev.spoocy.genius.core.data.sections;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.model.Sections;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class HighlightObject implements Sections.Highlight {

    @NotNull
    private final String property;

    @NotNull
    private final String value;

    @NotNull
    private final Boolean snippet;

    @NotNull
    private final List<Sections.Range> ranges;

    public HighlightObject(
            @NotNull String property,
            @NotNull String value,
            @NotNull Boolean snippet,
            @NotNull List<Sections.Range> ranges
    ) {
        this.property = property;
        this.value = value;
        this.snippet = snippet;
        this.ranges = ranges;
    }

    @Override
    public @NotNull String getProperty() {
        return this.property;
    }

    @Override
    public @NotNull String getValue() {
        return this.value;
    }

    @Override
    public @NotNull Boolean isSnippet() {
        return this.snippet;
    }

    @Override
    public @NotNull List<Sections.Range> getRanges() {
        return this.ranges;
    }

    public static class Parser extends AbstractModelParser<Sections.Highlight> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        protected Sections.Highlight parse0(@NotNull Config data) {
            return new HighlightObject(
                    data.getString("property"),
                    data.getString("value"),
                    data.getBoolean("snippet"),
                    RangeObject.Parser.INSTANCE.parseList(data.getSectionArray("ranges"))
            );
        }
    }

}
