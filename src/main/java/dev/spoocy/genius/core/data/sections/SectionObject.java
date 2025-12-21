package dev.spoocy.genius.core.data.sections;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.http.SearchType;
import dev.spoocy.genius.model.Sections;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SectionObject implements Sections.Section {

    @NotNull
    private final SearchType type;

    @NotNull
    private final List<Sections.Hit> hits;

    public SectionObject(@NotNull SearchType type, @NotNull List<Sections.Hit> hits) {
        this.type = type;
        this.hits = hits;
    }


    @Override
    public @NotNull SearchType getType() {
        return this.type;
    }

    @Override
    public @NotNull List<Sections.Hit> getHits() {
        return this.hits;
    }

    public static class Parser extends AbstractModelParser<Sections.Section> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        protected Sections.Section parse0(@NotNull Config data) {
            return new SectionObject(
                    SearchType.fromKey(
                            data.getString("type")
                    ),
                    HitObject.Parser.INSTANCE.parseList(
                            data.getSectionArray("hits")
                    )
            );
        }
    }

}
