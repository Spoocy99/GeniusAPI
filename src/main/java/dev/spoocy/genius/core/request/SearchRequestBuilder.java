package dev.spoocy.genius.core.request;

import dev.spoocy.common.config.Document;
import dev.spoocy.common.config.documents.JsonDocument;
import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.data.Search;
import dev.spoocy.genius.exception.GeniusException;
import org.json.JSONObject;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SearchRequestBuilder extends Mono<Search> {

    private static final String BASE_URL = "search?q=";

    private final GeniusClient client;
    private String query;

    public SearchRequestBuilder(GeniusClient client) {
        this.client = client;
    }

    public SearchRequestBuilder setQuery(String query) {
        this.query = query;
        return this;
    }

    @Override
    public void subscribe(CoreSubscriber<? super Search> coreSubscriber) {
        if(this.query == null) {
            coreSubscriber.onError(new IllegalArgumentException("Query for Search must be set!"));
            return;
        }

        String endpoint = BASE_URL + this.query;

        try {
            InputStream stream = client.execute(endpoint);
            Mono.just(buildSearch(stream))
                    .doOnError(coreSubscriber::onError)
                    .subscribe(coreSubscriber);
        } catch (Exception e) {
            coreSubscriber.onError(e);
        }
    }

    private Search buildSearch(InputStream stream) {
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
            throw new GeniusException("Error while searching " + status + ": " + json.getJSONObject("meta").getString("message"));
        }

        JSONObject search = json.getJSONObject("response");

        try {
            Document data = Document.readObject(JsonDocument.class, search);
            return new Search(data);
        } catch (Exception e) {
            throw new GeniusException("Could not parse Search data.", e);
        }
    }
}
