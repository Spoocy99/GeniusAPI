package dev.spoocy.genius.data;

import dev.spoocy.common.config.Config;
import dev.spoocy.common.config.Document;
import lombok.Getter;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Getter
public class Album {

    private String apiPath;
    private String coverArtUrl;
    private String fullTitle;
    private long id;
    private String name;
    private String releaseDateForDisplay;
    private String url;
    private Artist artist;

    public Album(Config data) {
        this.apiPath = data.getString("api_path", "null");
        this.coverArtUrl = data.getString("cover_art_url", "null");
        this.fullTitle = data.getString("full_title", "nul");
        this.id = data.getLong("id", 0L);
        this.name = data.getString("name", "null");
        this.releaseDateForDisplay = data.getString("release_date_for_display", "null");
        this.url = data.getString("url", "null");
        this.artist = new Artist(data.getSection("artist"));
    }

    @Override
    public String toString() {
        return "Album{" +
                "apiPath='" + apiPath + '\'' +
                ", coverArtUrl='" + coverArtUrl + '\'' +
                ", fullTitle='" + fullTitle + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", releaseDateForDisplay='" + releaseDateForDisplay + '\'' +
                ", url='" + url + '\'' +
                ", artist=" + artist +
                '}';
    }
}
