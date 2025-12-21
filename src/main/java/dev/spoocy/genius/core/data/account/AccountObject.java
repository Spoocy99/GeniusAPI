package dev.spoocy.genius.core.data.account;

import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.core.data.artists.ArtistObject;
import dev.spoocy.genius.model.Account;
import dev.spoocy.genius.model.Artist;
import dev.spoocy.genius.model.CurrentUserMetaData;
import dev.spoocy.genius.model.User;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Map;

/**
 * Implementation for {@link Account}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AccountObject extends UserObject implements Account {

    @NotNull
    private final Map<TextFormat, String> aboutMe;

    @Nullable
    private final String email;

    @Nullable
    private final Integer followedUsersCount;

    @Nullable
    private final String iqForDisplay;

    @Nullable
    private final Integer unreadGroupsInboxCount;

    @Nullable
    private final Integer unreadMainActivityInboxCount;

    @Nullable
    private final Integer unreadNewsfeedInboxCount;

    @Nullable
    private final Integer unreadMessagesCount;

    @Nullable
    private final Artist artist;

    public AccountObject(
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
            @NotNull CurrentUserMetaData currentUserMetaData,
            @NotNull Map<TextFormat, String> aboutMe,
            @Nullable String email,
            @Nullable Integer followedUsersCount,
            @Nullable String iqForDisplay,
            @Nullable Integer unreadGroupsInboxCount,
            @Nullable Integer unreadMainActivityInboxCount,
            @Nullable Integer unreadNewsfeedInboxCount,
            @Nullable Integer unreadMessagesCount,
            @Nullable Artist artist
    ) {
        super(apiPath, avatars, headerImageUrl, humanReadableRoleForDisplay, id, iq, login, name, roleForDisplay, url, currentUserMetaData);
        this.aboutMe = aboutMe;
        this.email = email;
        this.followedUsersCount = followedUsersCount;
        this.iqForDisplay = iqForDisplay;
        this.unreadGroupsInboxCount = unreadGroupsInboxCount;
        this.unreadMainActivityInboxCount = unreadMainActivityInboxCount;
        this.unreadNewsfeedInboxCount = unreadNewsfeedInboxCount;
        this.unreadMessagesCount = unreadMessagesCount;
        this.artist = artist;
    }


    @Override
    public @Nullable String getAboutMe(@NotNull TextFormat format) {
        String d = this.aboutMe.get(format);
        if (d == null) {
            throw new IllegalArgumentException("AboutMe does not contain format: " + format.getKey());
        }
        return d;
    }

    @Override
    public @Nullable String getEmail() {
        return this.email;
    }

    @Override
    public @Nullable Integer getFollowedUsersCount() {
        return this.followedUsersCount;
    }

    @Override
    public @Nullable String getIqForDisplay() {
        return this.iqForDisplay;
    }

    @Override
    public @Nullable Integer getUnreadGroupsInboxCount() {
        return this.unreadGroupsInboxCount;
    }

    @Override
    public @Nullable Integer getUnreadMainActivityInboxCount() {
        return this.unreadMainActivityInboxCount;
    }

    @Override
    public @Nullable Integer getUnreadNewsfeedInboxCount() {
        return this.unreadNewsfeedInboxCount;
    }

    @Override
    public @Nullable Integer getUnreadMessagesCount() {
        return this.unreadMessagesCount;
    }

    @Override
    public @Nullable Artist getArtist() {
        return this.artist;
    }

    public static final class Parser extends AbstractModelParser<Account> {

        public static final Parser INSTANCE = new Parser();

        private Parser() {
        }

        @Override
        public Account parseResponse(String json) {
            return this.parse(
                    new JSONObject(json)
                            .getJSONObject("response")
                            .getJSONObject("user")
            );
        }

        @Override
        public Account parse0(@NotNull Config data) {
            // reuse UserObject parser to extract base user fields
            UserObject base = (UserObject) UserObject.Parser.INSTANCE.parse(data);

            Artist artist = null;
            if (data.isSet("artist")) {
                artist = ArtistObject.Parser.INSTANCE.parse(data.getSection("artist"));
            }

            return new AccountObject(
                    base.getApiPath(),
                    base.getAvatars(),
                    base.getHeaderImageUrl(),
                    base.getHumanReadableRoleForDisplay(),
                    base.getId(),
                    base.getIq(),
                    base.getLogin(),
                    base.getName(),
                    base.getRoleForDisplay(),
                    base.getUrl(),
                    base.getCurrentUserMetaData(),
                    parseFormats(data.getSection("about_me")),
                    stringOrNull(data, "email"),
                    integerOrNull(data, "followed_users_count"),
                    stringOrNull(data, "iq_for_display"),
                    integerOrNull(data, "unread_groups_inbox_count"),
                    integerOrNull(data, "unread_main_activity_inbox_count"),
                    integerOrNull(data, "unread_newsfeed_inbox_count"),
                    integerOrNull(data, "unread_messages_count"),
                    artist
            );
        }
    }

}
