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

public interface Referent extends GeniusApiDataObject {

    @NotNull
    String getType();

    @Nullable
    Long getAnnotationId();

    @Nullable
    Long getAnnotatorId();

    @NotNull
    String getAnnotatorLogin();

    @NotNull
    String getApiPath();

    @NotNull
    String getClassification();

    @NotNull
    Boolean isFeatured();

    @NotNull
    String getFragment();

    @NotNull
    Long getId();

    @NotNull
    Boolean isDescription();

    @NotNull
    String getPath();

    @NotNull
    Range getRange();

    @NotNull
    Long getSongId();

    @NotNull
    String getUrl();

    @NotNull
    Long[] getVerifiedAnnotatorIds();

    @NotNull
    Annotatable getAnnotatable();

    @Nullable
    Annotation[] getAnnotations();


    interface Range {

        @NotNull
        String getStart();

        @NotNull
        Integer getStartOffset();

        @NotNull
        String getEnd();

        @NotNull
        Integer getEndOffset();

        @NotNull
        String getBefore();

        @NotNull
        String getAfter();

        @NotNull
        String getContent();

    }


    interface Annotatable {

        @NotNull
        String getApiPath();

        @NotNull
        String getContext();

        @NotNull
        Long getId();

        @NotNull
        String getImageUrl();

        @NotNull
        String getLinkTitle();

        @NotNull
        String getTitle();

        @NotNull
        String getType();

        @NotNull
        String getUrl();

    }

}
