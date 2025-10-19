package dev.spoocy.genius.core.request;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.exception.GeniusException;
import dev.spoocy.utils.common.exceptions.WrappedException;
import dev.spoocy.utils.config.Config;
import dev.spoocy.utils.config.documents.JsonConfig;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class RequestBuilder<T> extends Mono<T> {

    private final GeniusClient client;

    public RequestBuilder(@NotNull GeniusClient client) {
        this.client = client;
    }

    protected GeniusClient getClient() {
        return client;
    }

    protected abstract String buildEndpointUrl();

    protected abstract T buildObject(@NotNull Config data);

    protected abstract JSONObject getDataObject(@NotNull JSONObject response);

    public T execute() throws GeniusException {
        try {

            String endpoint = buildEndpointUrl();
            InputStream data = getClient().execute(endpoint);

            if(data == null) {
                throw new GeniusException("No data received from Genius API.");
            }

            Config config;

            try {
                config = buildData(data);
            } catch (Exception e) {
                throw new GeniusException("Could not parse data from Genius API for endpoint: " + endpoint, e);
            }

            return buildObject(config);
        } catch (Exception e) {
            WrappedException.rethrow(e);
        }

        return null;
    }

    public void subscribe(@NotNull CoreSubscriber<? super T> coreSubscriber) {
        Mono.just(execute())
                .doOnError(coreSubscriber::onError)
                .subscribe(coreSubscriber);
    }

    private Config buildData(@NotNull InputStream data) {
        InputStreamReader reader = new InputStreamReader(data);
        BufferedReader br = new BufferedReader(reader);

        String line;
        StringBuilder response = new StringBuilder();

        try {
            while((line =  br.readLine()) != null){

                response.append(line);
            }
            data.close();

        } catch (Exception e) {
            throw new GeniusException("Could not read response from Genius API.", e);
        }

        JSONObject json;
        try {
            json = new JSONObject(response.toString());
        } catch (Throwable e) {
            throw new GeniusException("Wrong API Response: " + response, e);
        }

        if(!json.has("meta")) {
            throw new GeniusException("No Body found. Please report this to the developer.");
        }

        int status = json.getJSONObject("meta").getInt("status");

        if(status < 200 || status >= 400) {
            throw new GeniusException("Error while getting Song " + status + ": " + json.getJSONObject("meta").getString("message"));
        }

        try {
            JSONObject song = getDataObject(json);
            return new JsonConfig(song);
        } catch (Throwable e) {
            throw new GeniusException("Could not parse JSON data.", e);
        }
    }

}
