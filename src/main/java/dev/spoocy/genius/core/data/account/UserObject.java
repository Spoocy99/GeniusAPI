package dev.spoocy.genius.core.data.account;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.model.CurrentUserMetaData;
import dev.spoocy.genius.model.User;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation for {@link User}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class UserObject implements User {

    /**
     * Path: user.api_path
     */
    @NotNull
    private final String apiPath;

    /**
     * Path: user.avatar
     *
     * <ul>
     *     <li>user.avatar.{@code type}.url</li>
     *     <li>user.avatar.{@code type}.bounding_box.width</li>
     *     <li>user.avatar.{@code type}.bounding_box.height</li>
     * </ul>
     *
     */
    @NotNull
    private final User.Avatars avatars;

    /**
     * Path: user.header_image_url
     */
    @NotNull
    private final String headerImageUrl;

    /**
     * Path: user.human_readable_role_for_display
     */
    @NotNull
    private final String humanReadableRoleForDisplay;

    /**
     * Path: user.id
     */
    @NotNull
    private final Long id;

    /**
     * Path: user.iq
     */
    @NotNull
    private final Long iq;

    /**
     * Path: user.login
     */
    @NotNull
    private final String login;

    /**
     * Path: user.name
     */
    @NotNull
    private final String name;

    /**
     * Path: user.role_for_display
     */
    @NotNull
    private final String roleForDisplay;

    /**
     * Path: user.url
     */
    @NotNull
    private final String url;

    /**
     * Path: user.current_user_metadata
     */
    @NotNull
    private final CurrentUserMetaData currentUserMetaData;

    public UserObject(
            @NotNull String apiPath,
            @NotNull User.Avatars avatars,
            @NotNull String headerImageUrl,
            @NotNull String humanReadableRoleForDisplay,
            @NotNull Long id,
            @NotNull Long iq,
            @NotNull String login,
            @NotNull String name,
            @NotNull String roleForDisplay,
            @NotNull String url,
            @NotNull CurrentUserMetaData currentUserMetaData
    ) {
        this.apiPath = apiPath;
        this.avatars = avatars;
        this.headerImageUrl = headerImageUrl;
        this.humanReadableRoleForDisplay = humanReadableRoleForDisplay;
        this.id = id;
        this.iq = iq;
        this.login = login;
        this.name = name;
        this.roleForDisplay = roleForDisplay;
        this.url = url;
        this.currentUserMetaData = currentUserMetaData;
    }

    @Override
    public @NotNull String getApiPath() {
        return this.apiPath;
    }

    @Override
    public @NotNull Avatars getAvatars() {
        return this.avatars;
    }

    @Override
    public @NotNull String getHeaderImageUrl() {
        return this.headerImageUrl;
    }

    @Override
    public @NotNull String getHumanReadableRoleForDisplay() {
        return this.humanReadableRoleForDisplay;
    }

    @Override
    public @NotNull Long getId() {
        return this.id;
    }

    @Override
    public @NotNull Long getIq() {
        return this.iq;
    }

    @Override
    public @NotNull String getLogin() {
        return this.login;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull String getRoleForDisplay() {
        return this.roleForDisplay;
    }

    @Override
    public @NotNull String getUrl() {
        return this.url;
    }

    @Override
    public @NotNull CurrentUserMetaData getCurrentUserMetaData() {
        return this.currentUserMetaData;
    }

    public static final class Parser extends AbstractModelParser<User> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {}

        @Override
        public User parse0(@NotNull Config data) {
            return new UserObject(
                    data.getString("api_path", ""),
                    AvatarsObject.Parser.INSTANCE.parse(data.getSection("avatar")),
                    data.getString("header_image_url", ""),
                    data.getString("human_readable_role_for_display", ""),
                    data.getLong("id", 0L),
                    data.getLong("iq", 0L),
                    data.getString("login", ""),
                    data.getString("name", ""),
                    data.getString("role_for_display", ""),
                    data.getString("url", ""),
                    CurrentUserMetaDataObject.Parser.INSTANCE.parse(data.getSection("current_user_metadata"))
            );
        }

    }

}
