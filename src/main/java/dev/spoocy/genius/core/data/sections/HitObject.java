package dev.spoocy.genius.core.data.sections;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.songs.SongObject;
import dev.spoocy.genius.model.Sections;
import dev.spoocy.genius.model.Song;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class HitObject implements Sections.Hit {

    @NotNull
    private final List<Sections.Highlight> ranges;

    @NotNull
    private final String index;

    @NotNull
    private final String type;

    @NotNull
    private final Song result;

    public HitObject(
            @NotNull List<Sections.Highlight> ranges,
            @NotNull String index,
            @NotNull String type,
            @NotNull Song result
    ) {
        this.ranges = ranges;
        this.index = index;
        this.type = type;
        this.result = result;
    }

    @Override
    public @NotNull List<Sections.Highlight> getHighlights() {
        return this.ranges;
    }

    @Override
    public @NotNull String getIndex() {
        return this.index;
    }

    @Override
    public @NotNull String getType() {
        return this.type;
    }

    @Override
    public @NotNull Song getResult() {
        return this.result;
    }

    public static class Parser extends AbstractModelParser<Sections.Hit> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        protected Sections.Hit parse0(@NotNull Config data) {
            return new HitObject(
                    HighlightObject.Parser.INSTANCE.parseList(data.getSectionArray("highlights")),
                    data.getString("index"),
                    data.getString("type"),
                    SongObject.Parser.INSTANCE.parse(data.getSection("result"))
            );
        }
    }

}
