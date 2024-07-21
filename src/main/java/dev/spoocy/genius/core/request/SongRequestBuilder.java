package dev.spoocy.genius.core.request;

import dev.spoocy.common.config.Config;
import dev.spoocy.common.config.Document;
import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.data.Song;
import dev.spoocy.genius.core.TextFormat;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SongRequestBuilder extends RequestBuilder<Song> {

    private static final String BASE_URL = "songs/";
    private String id;
    private final List<TextFormat> formats;

    public SongRequestBuilder(GeniusClient client) {
        super(client);
        this.formats = new ArrayList<>();
    }

    @Override
    protected JSONObject getDataObject(JSONObject response) {
        return response.getJSONObject("response").getJSONObject("song");
    }

    @Override
    protected String buildEndpointUrl() {
        if(this.id == null) {
            throw new IllegalArgumentException("ID of Song must be set!");
        }

        String endpoint = BASE_URL + this.id;
        if(!this.formats.isEmpty()) {
            endpoint += "?text_format=" + String.join(",", this.formats.stream().map(TextFormat::getFormat).toArray(String[]::new));
        }

        return endpoint;
    }

    @Override
    protected Song buildObject(Config data) {
        return new Song(data);
    }

    public SongRequestBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public SongRequestBuilder addFormat(TextFormat format) {
        if(!this.formats.contains(format)) {
            this.formats.add(format);
        }
        return this;
    }

}
