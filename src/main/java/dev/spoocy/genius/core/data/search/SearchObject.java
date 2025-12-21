package dev.spoocy.genius.core.data.search;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.songs.SongObject;
import dev.spoocy.genius.model.Search;
import dev.spoocy.genius.model.Song;
import dev.spoocy.utils.config.Config;
import dev.spoocy.utils.config.SectionArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for {@link Search}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SearchObject implements Search {

    @NotNull
    private final List<Hit> hits;

    public SearchObject(
            @NotNull List<Hit> hits
    ) {
        this.hits = hits;
    }

    @Override
    public @NotNull List<Hit> getHits() {
        return this.hits;
    }

    public static final class HitObject implements Search.Hit {

        @NotNull
        private final List<Highlight> highlights;

        @NotNull
        private final String index;

        @NotNull
        private final String type;

        @NotNull
        private final Song result;

        public HitObject(
                @NotNull List<Highlight> highlights,
                @NotNull String index,
                @NotNull String type,
                @NotNull Song result
        ) {
            this.highlights = highlights;
            this.index = index;
            this.type = type;
            this.result = result;
        }


        @Override
        public @NotNull List<Highlight> getHighlights() {
            return this.highlights;
        }

        @Override
        public @NotNull String getIndex() {
            return this.index;
        }

        @Override
        public @NotNull String getType() {
            return this.type;
        }

        @Override
        public @NotNull Song getResult() {
            return this.result;
        }

    }

    public static class HighlightObject implements Highlight {

        @Nullable
        private final String property;

        @Nullable
        private final String value;

        @Nullable
        private final Boolean snippet;

        @Nullable
        private final List<Range> ranges;

        public HighlightObject(
                @Nullable String property,
                @Nullable String value,
                @Nullable Boolean snippet,
                @Nullable List<Range> ranges
        ) {
            this.property = property;
            this.value = value;
            this.snippet = snippet;
            this.ranges = ranges;
        }

        @Override
        public @Nullable String getProperty() {
            return this.property;
        }

        @Override
        public @Nullable String getValue() {
            return this.value;
        }

        @Override
        public @Nullable Boolean isSnippet() {
            return this.snippet;
        }

        @Override
        public List<Range> getRanges() {
            return this.ranges;
        }
    }

    public static class RangeObject implements Range {

        @NotNull
        private final Integer start;
        @NotNull
        private final Integer end;

        public RangeObject(@NotNull Integer start, @NotNull Integer end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public @NotNull Integer getStart() {
            return this.start;
        }

        @Override
        public @NotNull Integer getEnd() {
            return this.end;
        }
    }

    public static final class Parser extends AbstractModelParser<Search> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        public Search parse0(@NotNull Config data) {
            List<Hit> hits = new ArrayList<>();

            if (data.isSet("hits")) {

                SectionArray<? extends Config> hitArray = data.getSectionArray("hits");

                for (int i = 0; i < hitArray.length(); i++) {
                    Config hit = hitArray.get(i);
                    hits.add(parseHit(hit));
                }
            }

            return new SearchObject(hits);
        }

        private HitObject parseHit(@NotNull Config hit) {
            Config result = hit.getSection("result");
            Song s = SongObject.Parser.INSTANCE.parse(result);

            List<Highlight> highlights = new ArrayList<>();

            if (hit.isSet("highlights")) {
                highlights = new ArrayList<>();
                SectionArray<? extends Config> highlightArray = hit.getSectionArray("highlights");

                for (int i = 0; i < highlightArray.length(); i++) {
                    Config highlight = highlightArray.get(i);
                    highlights.add(parseHighlight(highlight));
                }
            }

            return new HitObject(
                    highlights,
                    hit.getString("index", ""),
                    hit.getString("type", ""),
                    s);
        }

        private Highlight parseHighlight(@NotNull Config highlight) {
            List<Range> ranges = new ArrayList<>();
            if (highlight.isSet("ranges")) {
                SectionArray<? extends Config> rangesArray = highlight.getSectionArray("ranges");

                for (int i = 0; i < rangesArray.length(); i++) {
                    Config range = rangesArray.get(i);

                    ranges.add(new RangeObject(
                            range.getInt("start", 0),
                            range.getInt("end", 0)
                    ));

                }
            }

            return new HighlightObject(
                    stringOrNull(highlight, "property"),
                    stringOrNull(highlight, "value"),
                    booleanOrNull(highlight, "snippet"),
                    ranges.isEmpty() ? null : ranges
            );
        }

    }

}

