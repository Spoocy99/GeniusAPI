package dev.spoocy.genius.core.request;

import dev.spoocy.common.config.Document;
import dev.spoocy.common.config.documents.JsonDocument;
import dev.spoocy.genius.data.Artist;
import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.exception.GeniusException;
import org.json.JSONObject;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class ArtistRequestBuilder extends Mono<Artist> {

    private static final String BASE_URL = "artists/";

    private final GeniusClient client;
    private String id;
    private final List<TextFormat> formats;

    public ArtistRequestBuilder(GeniusClient client) {
        this.client = client;
        this.formats = new ArrayList<>();
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

    @Override
    public void subscribe(CoreSubscriber<? super Artist> coreSubscriber) {
        if(this.id == null) {
            coreSubscriber.onError(new IllegalArgumentException("ID of Artist must be set!"));
            return;
        }

        String endpoint = BASE_URL + this.id;
        if(!this.formats.isEmpty()) {
            endpoint += "?text_format=" + String.join(",", this.formats.stream().map(TextFormat::getFormat).toArray(String[]::new));
        }

        try {
            InputStream stream = client.execute(endpoint);
            Mono.just(buildArtist(stream)).subscribe(coreSubscriber);
        } catch (Exception e) {
            coreSubscriber.onError(e);
        }
    }

    private Artist buildArtist(InputStream stream) {
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(reader);

        String line;
        StringBuilder response = new StringBuilder();

        try {
            while((line =  br.readLine()) != null){

                response.append(line);
            }
            stream.close();

        } catch (Exception e) {
            throw new GeniusException("Could not read response from Genius API.", e);
        }

        JSONObject json = new JSONObject(response.toString());

        if(!json.has("meta")) {
            throw new GeniusException("No Body found. Report this to the developer.");
        }

        int status = json.getJSONObject("meta").getInt("status");

        if(status < 200 || status >= 400) {
            throw new GeniusException("Error while getting Artist " + status + ": " + json.getJSONObject("meta").getString("message"));
        }

        JSONObject artist = json.getJSONObject("response").getJSONObject("artist");

        try {
            Document data = Document.readObject(JsonDocument.class, artist);
            return new Artist(data);
        } catch (Exception e) {
            throw new GeniusException("Could not parse Artist data.", e);
        }
    }

}
