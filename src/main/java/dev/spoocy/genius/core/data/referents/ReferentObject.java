package dev.spoocy.genius.core.data.referents;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.annotations.AnnotationObject;
import dev.spoocy.genius.model.Annotation;
import dev.spoocy.genius.model.Referent;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation for {@link Referent}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ReferentObject implements Referent {

    @NotNull
    private final String type;

    @Nullable
    private final Long annotationId;

    @Nullable
    private final Long annotatorId;

    @NotNull
    private final String annotatorLogin;

    @NotNull
    private final String apiPath;

    @NotNull
    private final String classification;

    @NotNull
    private final Boolean featured;

    @NotNull
    private final String fragment;

    @NotNull
    private final Long id;

    @NotNull
    private final Boolean description;

    @NotNull
    private final String path;

    @NotNull
    private final Range range;

    @NotNull
    private final Long songId;

    @NotNull
    private final String url;

    @NotNull
    private final Long[] verifiedAnnotatorIds;

    @NotNull
    private final Annotatable annotatable;

    @Nullable
    private final Annotation[] annotations;

    public ReferentObject(
            @NotNull String type,
            @Nullable Long annotationId,
            @Nullable Long annotatorId,
            @NotNull String annotatorLogin,
            @NotNull String apiPath,
            @NotNull String classification,
            @NotNull Boolean featured,
            @NotNull String fragment,
            @NotNull Long id,
            @NotNull Boolean description,
            @NotNull String path,
            @NotNull Range range,
            @NotNull Long songId,
            @NotNull String url,
            @NotNull Long[] verifiedAnnotatorIds,
            @NotNull Annotatable annotatable,
            @Nullable Annotation[] annotations
    ) {
        this.type = type;
        this.annotationId = annotationId;
        this.annotatorId = annotatorId;
        this.annotatorLogin = annotatorLogin;
        this.apiPath = apiPath;
        this.classification = classification;
        this.featured = featured;
        this.fragment = fragment;
        this.id = id;
        this.description = description;
        this.path = path;
        this.range = range;
        this.songId = songId;
        this.url = url;
        this.verifiedAnnotatorIds = verifiedAnnotatorIds;
        this.annotatable = annotatable;
        this.annotations = annotations;
    }

    @Override
    public @NotNull String getType() {
        return this.type;
    }

    @Override
    public @Nullable Long getAnnotationId() {
        return this.annotationId;
    }

    @Override
    public @Nullable Long getAnnotatorId() {
        return this.annotatorId;
    }

    @Override
    public @NotNull String getAnnotatorLogin() {
        return this.annotatorLogin;
    }

    @Override
    public @NotNull String getApiPath() {
        return this.apiPath;
    }

    @Override
    public @NotNull String getClassification() {
        return this.classification;
    }

    @Override
    public @NotNull Boolean isFeatured() {
        return this.featured;
    }

    @Override
    public @NotNull String getFragment() {
        return this.fragment;
    }

    @Override
    public @NotNull Long getId() {
        return this.id;
    }

    @Override
    public @NotNull Boolean isDescription() {
        return this.description;
    }

    @Override
    public @NotNull String getPath() {
        return this.path;
    }

    @Override
    public @NotNull Range getRange() {
        return this.range;
    }

    @Override
    public @NotNull Long getSongId() {
        return this.songId;
    }

    @Override
    public @NotNull String getUrl() {
        return this.url;
    }

    @Override
    public @NotNull Long[] getVerifiedAnnotatorIds() {
        return this.verifiedAnnotatorIds;
    }

    @Override
    public @NotNull Annotatable getAnnotatable() {
        return this.annotatable;
    }

    @Override
    public @Nullable Annotation[] getAnnotations() {
        return this.annotations;
    }

    public static final class Parser extends AbstractModelParser<Referent> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        public Referent parse0(@NotNull Config data) {

            // parse range
            Config rangeSection = data.getSection("range");
            Range range = new RangeData(
                    rangeSection.getString("start", ""),
                    rangeSection.getInt("start_offset", 0),
                    rangeSection.getString("end", ""),
                    rangeSection.getInt("end_offset", 0),
                    rangeSection.getString("before", ""),
                    rangeSection.getString("after", ""),
                    rangeSection.getString("content", "")
            );

            // parse annotatable
            Config annSection = data.getSection("annotatable");
            Annotatable annotatable = new AnnotatableData(
                    annSection.getString("api_path", ""),
                    annSection.getString("context", ""),
                    annSection.getLong("id", 0L),
                    annSection.getString("image_url", ""),
                    annSection.getString("link_title", ""),
                    annSection.getString("title", ""),
                    annSection.getString("type", ""),
                    annSection.getString("url", "")
            );

            // parse annotations array
            Annotation[] annotations = null;
            if (data.isSet("annotations")) {
                annotations = AnnotationObject.Parser.INSTANCE
                        .parseList(data.getSectionArray("annotations"))
                        .toArray(Annotation[]::new);
            }

            // parse verified_annotator_ids as long[] via section array (numbers)
            Long[] verifiedIds = data.getLongList("verified_annotator_ids")
                    .toArray(Long[]::new);

            return new ReferentObject(
                    data.getString("type", ""),
                    longOrNull(data, "annotation_id"),
                    longOrNull(data, "annotator_id"),
                    data.getString("annotator_login", ""),
                    data.getString("api_path", ""),
                    data.getString("classification", ""),
                    data.getBoolean("featured", false),
                    data.getString("fragment", ""),
                    data.getLong("id", 0L),
                    data.getBoolean("description", false),
                    data.getString("path", ""),
                    range,
                    data.getLong("song_id", 0L),
                    data.getString("url", ""),
                    verifiedIds,
                    annotatable,
                    annotations
            );
        }
    }

    private static final class RangeData implements Range {
        private final String start;
        private final Integer startOffset;
        private final String end;
        private final Integer endOffset;
        private final String before;
        private final String after;
        private final String content;

        private RangeData(
                @NotNull String start,
                @NotNull Integer startOffset,
                @NotNull String end,
                @NotNull Integer endOffset,
                @NotNull String before,
                @NotNull String after,
                @NotNull String content
        ) {
            this.start = start;
            this.startOffset = startOffset;
            this.end = end;
            this.endOffset = endOffset;
            this.before = before;
            this.after = after;
            this.content = content;
        }

        @Override
        public @NotNull String getStart() {
            return this.start;
        }

        @Override
        public @NotNull Integer getStartOffset() {
            return this.startOffset;
        }

        @Override
        public @NotNull String getEnd() {
            return this.end;
        }

        @Override
        public @NotNull Integer getEndOffset() {
            return this.endOffset;
        }

        @Override
        public @NotNull String getBefore() {
            return this.before;
        }

        @Override
        public @NotNull String getAfter() {
            return this.after;
        }

        @Override
        public @NotNull String getContent() {
            return this.content;
        }
    }

    private static final class AnnotatableData implements Annotatable {
        private final String apiPath;
        private final String context;
        private final Long id;
        private final String imageUrl;
        private final String linkTitle;
        private final String title;
        private final String type;
        private final String url;

        private AnnotatableData(
                @NotNull String apiPath,
                @NotNull String context,
                @NotNull Long id,
                @NotNull String imageUrl,
                @NotNull String linkTitle,
                @NotNull String title,
                @NotNull String type,
                @NotNull String url
        ) {
            this.apiPath = apiPath;
            this.context = context;
            this.id = id;
            this.imageUrl = imageUrl;
            this.linkTitle = linkTitle;
            this.title = title;
            this.type = type;
            this.url = url;
        }

        @Override
        public @NotNull String getApiPath() {
            return this.apiPath;
        }

        @Override
        public @NotNull String getContext() {
            return this.context;
        }

        @Override
        public @NotNull Long getId() {
            return this.id;
        }

        @Override
        public @NotNull String getImageUrl() {
            return this.imageUrl;
        }

        @Override
        public @NotNull String getLinkTitle() {
            return this.linkTitle;
        }

        @Override
        public @NotNull String getTitle() {
            return this.title;
        }

        @Override
        public @NotNull String getType() {
            return this.type;
        }

        @Override
        public @NotNull String getUrl() {
            return this.url;
        }
    }

}
