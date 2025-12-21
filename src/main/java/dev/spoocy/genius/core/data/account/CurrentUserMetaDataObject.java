package dev.spoocy.genius.core.data.account;

import dev.spoocy.genius.core.data.AbstractModelParser;
import dev.spoocy.genius.model.CurrentUserMetaData;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation for {@link CurrentUserMetaData}.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class CurrentUserMetaDataObject implements CurrentUserMetaData {

    /**
     * Path: current_user_meta_data.permissions
     */
    @NotNull
    private final String[] permissions;

    /**
     * Path: current_user_meta_data.excluded_permissions
     */
    @NotNull
    private final String[] excludedPermissions;

    /**
     * Path: current_user_meta_data.following
     */
    @Nullable
    private final Boolean following;

    /**
     * Path: current_user_meta_data.cosign
     */
    @Nullable
    private final Boolean cosign;

    /**
     * Path: current_user_meta_data.pyong
     */
    @Nullable
    private final Boolean pyong;

    /**
     * Path: current_user_meta_data.vote
     */
    @Nullable
    private final String vote;

    /**
     * Path: current_user_meta_data.features
     */
    @Nullable
    private final String[] features;

    protected CurrentUserMetaDataObject(
            @NotNull String[] permissions,
            @NotNull String[] excludedPermissions,
            @Nullable Boolean following,
            @Nullable Boolean cosign,
            @Nullable Boolean pyong,
            @Nullable String vote,
            @Nullable String[] features
    ) {

        this.permissions = permissions;
        this.excludedPermissions = excludedPermissions;
        this.following = following;
        this.cosign = cosign;
        this.pyong = pyong;
        this.vote = vote;
        this.features = features;
    }

    @Override
    public @NotNull String[] getPermissions() {
        return this.permissions;
    }

    @Override
    public @NotNull String[] getExcludedPermissions() {
        return this.excludedPermissions;
    }

    @Override
    public @Nullable Boolean isFollowing() {
        return this.following;
    }

    @Override
    public @Nullable Boolean isCosign() {
        return this.cosign;
    }

    @Override
    public @Nullable Boolean isPyong() {
        return this.pyong;
    }

    @Override
    public @Nullable String getVote() {
        return this.vote;
    }

    @Override
    public @Nullable String[] getFeatures() {
        return this.features;
    }

    public static class Parser extends AbstractModelParser<CurrentUserMetaData> {

        public static Parser INSTANCE = new Parser();

        private Parser() {}

        @Override
        public CurrentUserMetaData parse0(@NotNull Config data) {
            return new CurrentUserMetaDataObject(
                    data.getStringList("permissions").toArray(String[]::new),
                    data.getStringList("excluded_permissions").toArray(String[]::new),
                    booleanOrNull(data, "following"),
                    booleanOrNull(data, "cosign"),
                    booleanOrNull(data, "pyong"),
                    stringOrNull(data, "vote"),
                    stringArrayOrNull(data, "features")
            );
        }

    }

}
