package dev.spoocy.genius.core.data.referents;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.model.Referent;
import dev.spoocy.genius.model.Referents;
import dev.spoocy.utils.config.Config;
import dev.spoocy.utils.config.SectionArray;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for {@link Referents}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ReferentsObject implements Referents {

    private final List<Referent> referents;

    public ReferentsObject(@NotNull List<Referent> referents) {
        this.referents = referents;
    }

    @Override
    public List<Referent> getReferents() {
        return this.referents;
    }

    public static final class Parser extends AbstractModelParser<Referents> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {}

        @Override
        public Referents parse0(@NotNull Config data) {
            SectionArray<? extends Config> array = data.getSectionArray("referents");
            List<Referent> list = new ArrayList<>();
            for(int i = 0; i < array.length(); i++) {
                list.add(ReferentObject.Parser.INSTANCE.parse(array.get(i)));
            }
            return new ReferentsObject(list);
        }
    }

}

