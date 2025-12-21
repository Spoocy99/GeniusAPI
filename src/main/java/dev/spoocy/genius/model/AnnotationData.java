package dev.spoocy.genius.model;

import dev.spoocy.genius.core.data.GeniusApiDataObject;
import org.jetbrains.annotations.NotNull;

/**
 * An annotation is a piece of content about a part of a document. The document may be a
 * song (hosted on Genius) or a web page (hosted anywhere). The part of a document that an
 * annotation is attached to is called a referent.
 * <p>
 * Annotation data returned from the API includes both the substance of the annotation and the
 * necessary information for displaying it in its original context.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface AnnotationData extends GeniusApiDataObject {

    @NotNull
    Annotation getAnnotation();

    @NotNull
    Referent getReferent();

}
