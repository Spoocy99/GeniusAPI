package dev.spoocy.genius.model;

import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.data.GeniusApiDataObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

public interface Annotation extends GeniusApiDataObject {


    @NotNull
    String getApiPath();


    @NotNull
    String getBody(@NotNull TextFormat format);

    @NotNull
    Integer getCommentCount();

    @NotNull
    Boolean isCommunity();

    @NotNull
    String getCustomPreview();

    @NotNull
    Boolean isHasVoters();

    @NotNull
    Long getId();

    @NotNull
    Boolean isPinned();

    @NotNull
    String getShareUrl();

    @NotNull
    String getSource();

    @NotNull
    String getState();

    @NotNull
    String getUrl();

    @NotNull
    Boolean isVerified();

    @NotNull
    Integer getVotesTotal();

    @NotNull
    CurrentUserMetaData getCurrentUserMetaData();

    @NotNull
    List<Author> getAuthors();

    @NotNull
    String[] getCosignedBy();

    @NotNull
    String getRejectionComment();

    @NotNull
    User getVerifiedBy();

    interface Author {

        @NotNull
        Integer getAttribution();

        @NotNull
        String getPinnedRole();

        @NotNull
        User getUser();
    }

}
