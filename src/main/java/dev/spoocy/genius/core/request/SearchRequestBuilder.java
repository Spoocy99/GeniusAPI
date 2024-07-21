package dev.spoocy.genius.core.request;

import dev.spoocy.common.config.Config;
import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.data.Search;
import org.json.JSONObject;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class SearchRequestBuilder extends RequestBuilder<Search> {

    private static final String BASE_URL = "search?q=";
    private String query;

    public SearchRequestBuilder(GeniusClient client) {
        super(client);
    }

    @Override
    protected JSONObject getDataObject(JSONObject response) {
        return response.getJSONObject("response");
    }

    @Override
    protected String buildEndpointUrl() {
        if(this.query == null) {
            throw new IllegalArgumentException("Query for Search must be set!");
        }

        return BASE_URL + this.query;
    }

    @Override
    protected Search buildObject(Config data) {
        return new Search(data);
    }

    public SearchRequestBuilder setQuery(String query) {
        this.query = query;
        return this;
    }
}
