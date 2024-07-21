package dev.spoocy.genius.core.request;

import dev.spoocy.common.config.Config;
import dev.spoocy.common.config.documents.JsonConfig;
import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.exception.GeniusException;
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

    protected abstract JSONObject getDataObject(JSONObject response);

    protected abstract String buildEndpointUrl();

    protected abstract T buildObject(Config data);

    public T execute() throws GeniusException {
        try {
            InputStream data = getClient().execute(buildEndpointUrl());
            return buildObject(buildData(data));
        } catch (Exception e) {
            throw new GeniusException("Error while executing request.", e);
        }
    }

    public void subscribe(@NotNull CoreSubscriber<? super T> coreSubscriber) {
        try {
            InputStream data = getClient().execute(buildEndpointUrl());
            Mono.just(buildObject(buildData(data)))
                    .doOnError(coreSubscriber::onError)
                    .subscribe(coreSubscriber);
        } catch (Exception e) {
            coreSubscriber.onError(e);
        }
    }

    private Config buildData(InputStream data) {
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

        JSONObject json = new JSONObject(response.toString());

        if(!json.has("meta")) {
            throw new GeniusException("No Body found. Please report this to the developer.");
        }

        int status = json.getJSONObject("meta").getInt("status");

        if(status < 200 || status >= 400) {
            throw new GeniusException("Error while getting Song " + status + ": " + json.getJSONObject("meta").getString("message"));
        }

        JSONObject song = getDataObject(json);

        try {
            return Config.readObject(JsonConfig.class, song);
        } catch (Throwable e) {
            throw new GeniusException("Could not parse JSON data.", e);
        }
    }

}
