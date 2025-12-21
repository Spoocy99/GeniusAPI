package dev.spoocy.genius.model;

import dev.spoocy.genius.core.TextFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Account information includes general contact information
 * and Genius-specific details about a user.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface Account extends User {

    /**
     * Gets the values of 'user.about_me'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     * - {@link Account}
     * </ul>
     *
     * @return An array of permission strings.
     *
     * @throws IllegalArgumentException if the provided format is not available
     */
    @Nullable
    String getAboutMe(@NotNull TextFormat format);

    /**
     * Gets the values of 'user.email'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     * - {@link Account}
     * </ul>
     *
     * @return An array of permission strings.
     */
    @Nullable
    String getEmail();

    /**
     * Gets the values of 'user.followers_count'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     * - {@link Account}
     * </ul>
     *
     * @return An array of permission strings.
     */
    @Nullable
    Integer getFollowedUsersCount();

    /**
     * Gets the values of 'user.iq_for_display'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     * - {@link Account}
     * </ul>
     *
     * @return An array of permission strings.
     */
    @Nullable
    String getIqForDisplay();

    /**
     * Gets the values of 'user.unread_groups_inbox_count'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     * - {@link Account}
     * </ul>
     *
     * @return An array of permission strings.
     */
    @Nullable
    Integer getUnreadGroupsInboxCount();

    /**
     * Gets the values of 'user.unread_main_activity_inbox_count'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     * - {@link Account}
     * </ul>
     *
     * @return An array of permission strings.
     */
    @Nullable
    Integer getUnreadMainActivityInboxCount();

    /**
     * Gets the values of 'user.unread_newsfeed_inbox_count'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     * - {@link Account}
     * </ul>
     *
     * @return An array of permission strings.
     */
    @Nullable
    Integer getUnreadNewsfeedInboxCount();

    /**
     * Gets the values of 'user.unread_messages_count'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     * - {@link Account}
     * </ul>
     *
     * @return An array of permission strings.
     */
    @Nullable
    Integer getUnreadMessagesCount();

    /**
     * Gets the values of 'user.artist'.
     * <p>
     * Possibly available in the following requests:
     * <ul>
     * - {@link Account}
     * </ul>
     *
     * @return An array of permission strings.
     */
    @Nullable
    Artist getArtist();


}
