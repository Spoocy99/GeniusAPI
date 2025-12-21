package dev.spoocy.genius.core.data.annotations;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.referents.ReferentObject;
import dev.spoocy.genius.model.Annotation;
import dev.spoocy.genius.model.AnnotationData;
import dev.spoocy.genius.model.Referent;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation for {@link AnnotationData}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public class AnnotationDataObject implements AnnotationData {

    private final Annotation annotation;
    private final Referent referent;

    public AnnotationDataObject(@NotNull Annotation annotation, @NotNull Referent referent) {
        this.annotation = annotation;
        this.referent = referent;
    }

    @Override
    public @NotNull Annotation getAnnotation() {
        return this.annotation;
    }

    @Override
    public @NotNull Referent getReferent() {
        return this.referent;
    }

    public static final class Parser extends AbstractModelParser<AnnotationData> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {}

        @Override
        public AnnotationData parse0(@NotNull Config data) {
            return new AnnotationDataObject(
                    AnnotationObject.Parser.INSTANCE.parse(data.getSection("annotation")),
                    ReferentObject.Parser.INSTANCE.parse(data.getSection("referent"))
            );
        }
    }

}
