package dev.spoocy.genius.core.request;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.data.Search;
import dev.spoocy.utils.config.Config;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SearchRequestBuilder extends RequestBuilder<Search> {

    private static final String BASE_URL = "search?q=";
    private String query;

    public SearchRequestBuilder(@NotNull GeniusClient client) {
        super(client);
    }

    @Override
    protected String buildEndpointUrl() {
        if(this.query == null) {
            throw new IllegalArgumentException("Query for Search must be set!");
        }

        return BASE_URL + this.query;
    }

    @Override
    protected Search buildObject(@NotNull Config data) {
        return new Search(data);
    }

    @Override
    protected JSONObject getDataObject(@NotNull JSONObject response) {
        return response.getJSONObject("response");
    }

    public SearchRequestBuilder setQuery(@NotNull String query) {
        this.query = query;
        return this;
    }
}
