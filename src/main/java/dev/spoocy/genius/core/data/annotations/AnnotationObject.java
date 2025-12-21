package dev.spoocy.genius.core.data.annotations;

import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.account.CurrentUserMetaDataObject;
import dev.spoocy.genius.core.data.account.UserObject;
import dev.spoocy.genius.model.Annotation;
import dev.spoocy.genius.model.CurrentUserMetaData;
import dev.spoocy.genius.model.User;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Implementation for {@link Annotation}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AnnotationObject implements Annotation {

    /**
     * Path: response.annotation.api_path
     */
    @NotNull
    private final String apiPath;

    /**
     * Path: response.annotation.body.{format}
     */
    @NotNull
    private final Map<TextFormat, String> body;

    /**
     * Path: response.annotation.comment_count
     */

    @NotNull
    private final Integer commentCount;

    /**
     * Path: response.annotation.community
     */
    @NotNull
    private final Boolean community;

    /**
     * Path: response.annotation.custom_preview
     */
    @NotNull
    private final String customPreview;

    /**
     * Path: response.annotation.community
     */
    @NotNull
    private final Boolean hasVoters;

    /**
     * Path: response.annotation.id
     */
    @NotNull
    private final Long id;

    /**
     * Path: response.annotation.pinned
     */
    @NotNull
    private final Boolean pinned;

    /**
     * Path: response.annotation.share_url
     */
    @NotNull
    private final String shareUrl;

    /**
     * Path: response.annotation.source
     */
    @NotNull
    private final String source;

    /**
     * Path: response.annotation.state
     */
    @NotNull
    private final String state;

    /**
     * Path: response.annotation.url
     */
    @NotNull
    private final String url;

    /**
     * Path: response.annotation.verified
     */
    @NotNull
    private final Boolean verified;

    /**
     * Path: response.annotation.votes_total
     */
    @NotNull
    private final Integer votesTotal;

    /**
     * Path: response.annotation.current_user_metadata
     */
    @NotNull
    private final CurrentUserMetaData currentUserMetaData;

    /**
     * Path: response.annotation.authors
     */
    @NotNull
    private final List<Annotation.Author> authors;

    /**
     * Path: response.annotation.cosigned_by
     */
    @NotNull
    private final String[] cosignedBy;

    /**
     * Path: response.annotation.rejection_comment
     */
    @NotNull
    private final String rejectionComment;

    /**
     * Path: response.annotation.verified_by
     */
    @NotNull
    private final User verifiedBy;

    public AnnotationObject(
            @NotNull String apiPath,
            @NotNull Map<TextFormat, String> body,
            @NotNull Integer commentCount,
            @NotNull Boolean community,
            @NotNull String customPreview,
            @NotNull Boolean hasVoters,
            @NotNull Long id,
            @NotNull Boolean pinned,
            @NotNull String shareUrl,
            @NotNull String source,
            @NotNull String state,
            @NotNull String url,
            @NotNull Boolean verified,
            @NotNull Integer votesTotal,
            @NotNull CurrentUserMetaData currentUserMetaData,
            @NotNull List<Annotation.Author> authors,
            @NotNull String[] cosignedBy,
            @NotNull String rejectionComment,
            @NotNull User verifiedBy
    ) {
        this.apiPath = apiPath;
        this.body = body;
        this.commentCount = commentCount;
        this.community = community;
        this.customPreview = customPreview;
        this.hasVoters = hasVoters;
        this.id = id;
        this.pinned = pinned;
        this.shareUrl = shareUrl;
        this.source = source;
        this.state = state;
        this.url = url;
        this.verified = verified;
        this.votesTotal = votesTotal;
        this.currentUserMetaData = currentUserMetaData;
        this.authors = authors;
        this.cosignedBy = cosignedBy;
        this.rejectionComment = rejectionComment;
        this.verifiedBy = verifiedBy;
    }


    @Override
    public @NotNull String getApiPath() {
        return this.apiPath;
    }

    @Override
    public @NotNull String getBody(@NotNull TextFormat format) {
        String bodyContent = this.body.get(format);
        if(bodyContent == null) {
            throw new IllegalArgumentException("Body does not contain format: " + format.getKey());
        }
        return bodyContent;
    }

    @Override
    public @NotNull Integer getCommentCount() {
        return this.commentCount;
    }

    @Override
    public @NotNull Boolean isCommunity() {
        return this.community;
    }

    @Override
    public @NotNull String getCustomPreview() {
        return this.customPreview;
    }

    @Override
    public @NotNull Boolean isHasVoters() {
        return this.hasVoters;
    }

    @Override
    public @NotNull Long getId() {
        return this.id;
    }

    @Override
    public @NotNull Boolean isPinned() {
        return this.pinned;
    }

    @Override
    public @NotNull String getShareUrl() {
        return this.shareUrl;
    }

    @Override
    public @NotNull String getSource() {
        return this.source;
    }

    @Override
    public @NotNull String getState() {
        return this.state;
    }

    @Override
    public @NotNull String getUrl() {
        return this.url;
    }

    @Override
    public @NotNull Boolean isVerified() {
        return this.verified;
    }

    @Override
    public @NotNull Integer getVotesTotal() {
        return this.votesTotal;
    }

    @Override
    public @NotNull CurrentUserMetaData getCurrentUserMetaData() {
        return this.currentUserMetaData;
    }

    @Override
    public @NotNull List<Annotation.Author> getAuthors() {
        return this.authors;
    }

    @Override
    public String[] getCosignedBy() {
        return this.cosignedBy;
    }

    @Override
    public @NotNull String getRejectionComment() {
        return this.rejectionComment;
    }

    @Override
    public @NotNull User getVerifiedBy() {
        return this.verifiedBy;
    }

    public static final class Parser extends AbstractModelParser<Annotation> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        public Annotation parse0(@NotNull Config data) {
            return new AnnotationObject(
                    data.getString("api_path", ""),
                    parseFormats(data.getSection("body")),
                    data.getInt("comment_count", 0),
                    data.getBoolean("community", false),
                    data.getString("custom_preview", ""),
                    data.getBoolean("has_voters", false),
                    data.getLong("id", 0L),
                    data.getBoolean("pinned", false),
                    data.getString("share_url", ""),
                    data.getString("source", ""),
                    data.getString("state", ""),
                    data.getString("url", ""),
                    data.getBoolean("verified", false),
                    data.getInt("votes_total", 0),
                    CurrentUserMetaDataObject.Parser.INSTANCE.parse(data.getSection("current_user_metadata")),
                    AuthorObject.Parser.INSTANCE.parseList(data.getSectionArray("authors")),
                    stringArrayOrNull(data, "cosigned_by"),
                    data.getString("rejection_comment", ""),
                    UserObject.Parser.INSTANCE.parse(data.getSection("verified_by"))
            );
        }

    }

}
