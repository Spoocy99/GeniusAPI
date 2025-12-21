package dev.spoocy.genius.core.data.sections;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.model.Sections;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SectionsObject implements Sections {

    @NotNull
    private final List<Section> hits;

    @NotNull
    private final Integer nextPage;

    public SectionsObject(@NotNull List<Section> hits, @NotNull Integer nextPage) {
        this.hits = hits;
        this.nextPage = nextPage;
    }

    @Override
    public @NotNull List<Section> getSections() {
        return this.hits;
    }

    @Override
    public @NotNull Integer getNextPage() {
        return this.nextPage;
    }

    public static class Parser extends AbstractModelParser<Sections> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        protected Sections parse0(@NotNull Config data) {
            return new SectionsObject(
                    SectionObject.Parser.INSTANCE.parseList(data.getSectionArray("sections")),
                    data.getInt("next_page")
            );
        }
    }

}
