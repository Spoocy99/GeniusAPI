package dev.spoocy.genius.core.data.lyrics;

import dev.spoocy.genius.model.Lyrics;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class LyricsObject implements Lyrics {

    @NotNull
    private final String title;

    @NotNull
    private final String plain;

    @NotNull
    private final List<Part> parts;

    public LyricsObject(
            @NotNull String title,
            @NotNull String plain
    ) {

        this.title = title;
        this.plain = normalize(plain);
        this.parts = splitToParts(this.plain);
    }

    @Override
    public @NotNull String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull String getPlain() {
        return this.plain;
    }

    @Override
    public @NotNull List<Part> getParts() {
        return this.parts;
    }

    private static String normalize(@NotNull String text) {
        return text
                .replaceAll("\n{2,}", "\n")         // collapse multiple blank lines and trim overall
                .trim()                                              // and remove leading whitespace at the beginning of
                .replaceAll("(?m)^\\s+", "");       // each line
    }

    private static List<Part> splitToParts(@NotNull String plain) {
        List<Part> parts = new ArrayList<>();
        String title = "";
        StringBuilder body = new StringBuilder();

        for (String line : plain.split("\n")) {
            if (line.startsWith("[")) { // new part starts now

                if (body.length() > 0) { // add recorded part to list
                    parts.add(
                            new LyricsPart(
                                    title,
                                    body.toString()
                                            .replace("[", "")
                                            .replace("]", "")
                                            .trim()
                            )
                    );
                    body = new StringBuilder();
                }

                title = line.substring(1, line.length() - 1);   // set title of new part
                continue;
            }

            body.append(line)
                    .append("\n");     // add line to currently recorded part
        }

        if (body.length() > 0) { // add last recorded part to list
            parts.add(
                    new LyricsPart(
                            title,
                            body.toString()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .trim()
                    )
            );
        }

        return parts;
    }

    private static class LyricsPart implements Lyrics.Part {

        @NotNull
        private final String title;

        @NotNull
        private final String text;

        private LyricsPart(@NotNull String title, @NotNull String text) {
            this.title = title;
            this.text = text;
        }

        @Override
        public @NotNull String getTitle() {
            return this.title;
        }

        @Override
        public @NotNull String getText() {
            return this.text;
        }
    }

}
