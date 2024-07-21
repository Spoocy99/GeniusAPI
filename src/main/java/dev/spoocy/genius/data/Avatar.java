package dev.spoocy.genius.data;

import dev.spoocy.common.config.Config;
import dev.spoocy.common.config.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
public class Avatar {

    private AvatarType tiny;
    private AvatarType thumb;
    private AvatarType small;
    private AvatarType medium;

    public Avatar(Config data) {
        Config tiny = data.getSection("tiny");
        this.tiny = new AvatarType(
                tiny.getString("url", "null"),
                tiny.getSection("bounding_box").getInt("width", 0),
                tiny.getSection("bounding_box").getInt("height", 0)
        );

        Config thumb = data.getSection("thumb");
        this.thumb = new AvatarType(
                thumb.getString("url", "null"),
                thumb.getSection("bounding_box").getInt("width", 0),
                thumb.getSection("bounding_box").getInt("height", 0)
        );

        Config small = data.getSection("small");
        this.small = new AvatarType(
                small.getString("url", "null"),
                small.getSection("bounding_box").getInt("width", 0),
                small.getSection("bounding_box").getInt("height", 0)
        );

        Config medium = data.getSection("medium");
        this.medium = new AvatarType(
                medium.getString("url", "null"),
                medium.getSection("bounding_box").getInt("width", 0),
                medium.getSection("bounding_box").getInt("height", 0)
        );
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "tiny=" + tiny +
                ", thumb=" + thumb +
                ", small=" + small +
                ", medium=" + medium +
                '}';
    }

    @Getter
    @AllArgsConstructor
    private static class AvatarType {
        private String url;
        private int width;
        private int height;

        @Override
        public String toString() {
            return "AvatarType{" +
                    "url='" + url + '\'' +
                    ", width='" + width + '\'' +
                    ", height='" + height + '\'' +
                    '}';
        }
    }

}

