package dev.spoocy.genius.core.data.account;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.model.User;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation for {@link User.Avatars}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AvatarsObject implements User.Avatars {

    @NotNull
    private final User.Avatar tiny, thumb, small, medium;

    private AvatarsObject(
            @NotNull User.Avatar tiny,
            @NotNull User.Avatar thumb,
            @NotNull User.Avatar small,
            @NotNull User.Avatar medium
    ) {
        this.tiny = tiny;
        this.thumb = thumb;
        this.small = small;
        this.medium = medium;
    }

    @Override
    public User.@NotNull Avatar getTiny() {
        return this.tiny;
    }

    @Override
    public User.@NotNull Avatar getThumb() {
        return this.thumb;
    }

    @Override
    public User.@NotNull Avatar getSmall() {
        return this.small;
    }

    @Override
    public User.@NotNull Avatar getMedium() {
        return this.medium;
    }

    private static class AvatarObject implements User.Avatar {

        @NotNull
        private final String url;

        @NotNull
        private final Integer width;

        @NotNull
        private final Integer height;

        private AvatarObject(
                @NotNull String url,
                @NotNull Integer width,
                @NotNull Integer height
        ) {
            this.url = url;
            this.width = width;
            this.height = height;
        }

        @Override
        public @NotNull String getUrl() {
            return this.url;
        }

        @Override
        public @NotNull Integer getWidth() {
            return this.width;
        }

        @Override
        public @NotNull Integer getHeight() {
            return this.height;
        }
    }

    public static class Parser extends AbstractModelParser<User.Avatars> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        public User.Avatars parse0(@NotNull Config data) {
            User.Avatar tiny = new AvatarObject(
                    data.getString("tiny.url", ""),
                    data.getInt("tiny.width", 0),
                    data.getInt("tiny.height", 0)
            );
            User.Avatar thumb = new AvatarObject(
                    data.getString("thumb.url", ""),
                    data.getInt("thumb.width", 0),
                    data.getInt("thumb.height", 0)
            );
            User.Avatar small = new AvatarObject(
                    data.getString("small.url", ""),
                    data.getInt("small.width", 0),
                    data.getInt("small.height", 0)
            );
            User.Avatar medium = new AvatarObject(
                    data.getString("medium.url", ""),
                    data.getInt("medium.width", 0),
                    data.getInt("medium.height", 0)
            );
            return new AvatarsObject(tiny, thumb, small, medium);
        }
    }

}
