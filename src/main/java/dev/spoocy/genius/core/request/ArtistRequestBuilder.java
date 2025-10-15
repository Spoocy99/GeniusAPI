package dev.spoocy.genius.core.request;

import dev.spoocy.genius.data.Artist;
import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ArtistRequestBuilder extends RequestBuilder<Artist> {

    private static final String BASE_URL = "artists/";
    private String id;
    private final List<TextFormat> formats;

    public ArtistRequestBuilder(GeniusClient client) {
        super(client);
        this.formats = new ArrayList<>();
    }

    @Override
    protected JSONObject getDataObject(JSONObject response) {
        return response.getJSONObject("response").getJSONObject("artist");
    }

    @Override
    protected String buildEndpointUrl() {
        if(this.id == null) {
            throw new IllegalArgumentException("ID of Artist must be set!");
        }

        String endpoint = BASE_URL + this.id;
        if(!this.formats.isEmpty()) {
            endpoint += "?text_format=" + String.join(",", this.formats.stream().map(TextFormat::getFormat).toArray(String[]::new));
        }

        return endpoint;
    }

    @Override
    protected Artist buildObject(@NotNull Config data) {
        return new Artist(data);
    }

    public ArtistRequestBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public ArtistRequestBuilder addFormat(TextFormat format) {
        if(!this.formats.contains(format)) {
            this.formats.add(format);
        }
        return this;
    }

}
