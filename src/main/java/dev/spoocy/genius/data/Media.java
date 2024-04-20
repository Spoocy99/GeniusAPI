package dev.spoocy.genius.data;

import dev.spoocy.common.config.Document;
import lombok.Getter;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
public class Media {

    private String provider;
    private String type;
    private String url;

    private int start;
    private String nativeUri;
    private String attribution;

    public Media(Document data) {
        this.provider = data.getString("provider", "null");
        this.type = data.getString("type", "null");
        this.url = data.getString("url", "null");
        this.start = data.getInt("start", 0);
        this.nativeUri = data.getString("native_uri", "null");
        this.attribution = data.getString("attribution", "null");
    }

    @Override
    public String toString() {
        return "Media{" +
                "provider='" + provider + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", start=" + start +
                ", nativeUri='" + nativeUri + '\'' +
                ", attribution='" + attribution + '\'' +
                '}';
    }
}
