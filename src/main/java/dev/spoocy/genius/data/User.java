package dev.spoocy.genius.data;

import dev.spoocy.utils.config.Config;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
public class User {

    private String apiPath;
    private Avatar avatar;
    private String headerImageUrl;
    private String humanReadableRoleForDisplay;
    private long id;
    private long iq;
    private String login;
    private String name;
    private String roleForDisplay;
    private String url;

    public User(@NotNull Config data) {
        this.apiPath = data.getString("api_path", "null");
        this.avatar = new Avatar(data.getSection("avatar"));
        this.headerImageUrl = data.getString("header_image_url", "null");
        this.humanReadableRoleForDisplay = data.getString("human_readable_role_for_display", "null");
        this.id = data.getLong("id", 0L);
        this.iq = data.getLong("iq", 0L);
        this.login = data.getString("login", "null");
        this.name = data.getString("name", "null");
        this.roleForDisplay = data.getString("role_for_display", "null");
        this.url = data.getString("url", "null");
    }


    @Override
    public String toString() {
        return "User{" +
                "apiPath='" + apiPath + '\'' +
                ", avatar=" + avatar +
                ", headerImageUrl='" + headerImageUrl + '\'' +
                ", humanReadableRoleForDisplay='" + humanReadableRoleForDisplay + '\'' +
                ", id=" + id +
                ", iq=" + iq +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", roleForDisplay='" + roleForDisplay + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
