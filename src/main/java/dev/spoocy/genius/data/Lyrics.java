package dev.spoocy.genius.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class Lyrics {

    private final String title;
    private final String plain;
    private final List<LyricsPart> parts;

    public Lyrics(@NotNull String title, @NotNull String plain) {
        this.title = title;
        this.plain = plain;
        this.parts = new ArrayList<>();

        splitToParts();
    }

    /**
     * Title of the song.
     *
     * @return The title of the lyrics.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Plain text of the lyrics.
     *
     * @return The plain text of the lyrics.
     */
    public String getAsPlain() {
        return this.plain;
    }

    /**
     * Tries to extract different parts of the lyrics.
     * (e.g. Verse, Chorus, etc.)
     *
     * @return The lyrics split into parts.
     */
    public List<LyricsPart> getAsParts() {
        if(this.parts.isEmpty()) {
            splitToParts();
        }
        return this.parts;
    }

    private void splitToParts() {
        String title = "";
        StringBuilder body = new StringBuilder();

        for(String line : plain.split("\n")) {
            if(line.startsWith("[")) { // new part starts now

                if(body.length() > 0) { // add recorded part to list
                    parts.add(
                            new LyricsPart(title, body.toString().replace("[", "").replace("]", "").trim())
                    );
                    body = new StringBuilder();
                }

                title = line.substring(1, line.length() - 1);   // set title of new part
                continue;
            }

            body.append(line).append("\n");     // add line to currently recorded part
        }

        if(body.length() > 0) { // add last recorded part to list
            parts.add(
                    new LyricsPart(title, body.toString().replace("[", "").replace("]", "").trim())
            );
        }

    }

    @Override
    public String toString() {
        return "Lyrics{" +
                "title='" + title + ", " +
                "plain='" + plain + ", " +
                "parts=" + parts +
                '}';
    }

    @Getter
    @AllArgsConstructor
    public static class LyricsPart {
        private String title;
        private String text;
    }

}
