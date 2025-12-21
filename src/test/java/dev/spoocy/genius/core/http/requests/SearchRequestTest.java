package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.data.search.SearchObject;
import dev.spoocy.genius.core.http.IHttpManager;
import dev.spoocy.genius.model.Search;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SearchRequestTest implements ITest<Search> {

    @Override
    public String getMockDataFile() {
        return "get_search.json";
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        Search s = SearchObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(s);
    }

    @Test
    void testRequest() throws Exception {
        var request = new SearchRequest.Builder(GENIUS_CLIENT)
                .query("Kendrick")
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(mockHttpManager())
                .build();

        Search s = request.block();
        testDataIntegrity(s);
    }

    @Override
    public void testDataIntegrity(Search s) {
        assertNotNull(s);
        assertFalse(s.getHits().isEmpty());
        assertEquals("song", s.getHits().get(0).getType());
        assertEquals(10359264, s.getHits().get(0).getResult().getId());

    }
}
