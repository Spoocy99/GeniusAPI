package dev.spoocy.genius.model;

import dev.spoocy.genius.core.data.GeniusApiDataObject;

import java.util.List;

/**
 * Referents are the sections of a piece of content to which annotations are
 * attached. Each referent is associated with a web page or a song and may
 * have one or more annotations. Referents can be searched by the document
 * they are attached to or by the user that created them.
 * <p>
 * When a new annotation is created, either a referent is created
 * with it or that annotation is attached to an existing referent.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Referents extends GeniusApiDataObject {

    List<Referent> getReferents();

}
