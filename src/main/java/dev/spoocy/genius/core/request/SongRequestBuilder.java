package dev.spoocy.genius.core.request;

import dev.spoocy.common.config.Document;
import dev.spoocy.common.config.documents.JsonDocument;
import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.data.Song;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.exception.GeniusException;
import org.jetbrains.annotations.NotNull;
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

public class SongRequestBuilder extends Mono<Song> {

    private static final String BASE_URL = "songs/";

    private final GeniusClient client;
    private String id;
    private final List<TextFormat> formats;

    public SongRequestBuilder(GeniusClient client) {
        this.client = client;
        this.formats = new ArrayList<>();
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

    @Override
    public void subscribe(@NotNull CoreSubscriber<? super Song> coreSubscriber) {
        if(this.id == null) {
            coreSubscriber.onError(new IllegalArgumentException("ID of Song must be set!"));
            return;
        }

        String endpoint = BASE_URL + this.id;
        if(!this.formats.isEmpty()) {
            endpoint += "?text_format=" + String.join(",", this.formats.stream().map(TextFormat::getFormat).toArray(String[]::new));
        }

        try {
            InputStream stream = client.execute(endpoint);
            Mono.just(buildSong(stream)).subscribe(coreSubscriber);
        } catch (Exception e) {
            coreSubscriber.onError(e);
        }
    }

    private Song buildSong(InputStream stream) {
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
            throw new GeniusException("Error while getting Song " + status + ": " + json.getJSONObject("meta").getString("message"));
        }

        JSONObject song = json.getJSONObject("response").getJSONObject("song");

        try {
            Document data = Document.readObject(JsonDocument.class, song);
            return new Song(data);
        } catch (Exception e) {
            throw new GeniusException("Could not parse Song data.", e);
        }
    }

}
