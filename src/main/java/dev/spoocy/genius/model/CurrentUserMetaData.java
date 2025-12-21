package dev.spoocy.genius.model;

import dev.spoocy.genius.core.data.GeniusApiDataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Referents are the sections of a piece of content to which annotations are
 * attached. Each referent is associated with a web page or a song and may
 * have one or more annotations. Referents can be searched by the document
 * they are attached to or by the user that created them.
 * <p>
 * When a new annotation is created either a referent is created
 * with it or that annotation is attached to an existing referent.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface CurrentUserMetaData extends GeniusApiDataObject {

    /**
     * Gets the values of 'permissions'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     *     - {@code all data requests}.
     * </ul>
     *
     * @return An array of permission strings.
     */
    @NotNull
    String[] getPermissions();

    /**
     * Gets the values of excluded_permissions.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     *     - {@code all data requests}.
     * </ul>
     *
     * @return An array of excluded permission strings.
     */
    @NotNull
    String[] getExcludedPermissions();

    /**
     * Gets the value of 'interactions.following'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     *     - {@link User#getCurrentUserMetaData()}
     * </ul>
     */
    @Nullable
    Boolean isFollowing();

    /**
     * Gets the value of 'interactions.cosign'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     *     - {@link AnnotationData#getAnnotation()} -> {@link Annotation#getCurrentUserMetaData()}
     * </ul>
     *
     * @throws UnsupportedOperationException if this data is not available in the current context.
     */
    @Nullable
    Boolean isCosign();

    /**
     * Gets the value of 'interactions.pyong'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     *     - {@link AnnotationData#getAnnotation()} -> {@link Annotation#getCurrentUserMetaData()}
     * </ul>
     */
    @Nullable
    Boolean isPyong();

    /**
     * Gets the value of 'interactions.vote'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     *     - {@link AnnotationData#getAnnotation()} -> {@link Annotation#getCurrentUserMetaData()}
     * </ul>
     */
    @Nullable
    String getVote();

    /**
     * Gets the value of 'features'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     *     - {@link Account#getCurrentUserMetaData()}
     * </ul>
     */
    @Nullable
    String[] getFeatures();


}
