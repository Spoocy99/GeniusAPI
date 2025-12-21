package dev.spoocy.genius.model;

import dev.spoocy.genius.core.data.GeniusApiDataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface User extends GeniusApiDataObject {

    @NotNull
    String getApiPath();

    @NotNull
    Avatars getAvatars();

    @NotNull
    String getHeaderImageUrl();

    @Nullable
    String getHumanReadableRoleForDisplay();

    @NotNull
    Long getId();

    @NotNull
    Long getIq();

    @NotNull
    String getLogin();

    @NotNull
    String getName();

    @NotNull
    String getRoleForDisplay();

    @NotNull
    String getUrl();

    @NotNull
    CurrentUserMetaData getCurrentUserMetaData();


    interface Avatars {

        @NotNull
        Avatar getTiny();

        @NotNull
        Avatar getThumb();

        @NotNull
        Avatar getSmall();

        @NotNull
        Avatar getMedium();

    }


    interface Avatar {

        @NotNull
        String getUrl();

        @NotNull
        Integer getWidth();

        @NotNull
        Integer getHeight();

    }

}
