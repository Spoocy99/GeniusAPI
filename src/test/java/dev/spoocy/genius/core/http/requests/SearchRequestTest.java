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

public class SearchRequestTest {

    private String mockJsonRequest() throws IOException {
        var is = this.getClass().getClassLoader().getResourceAsStream("get_search.json");
        assertNotNull(is, "Test resource not found: get_search.json");
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        Search s = SearchObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(s);
    }

    @Test
    void testRequest() throws Exception {
        String json = mockJsonRequest();
        IHttpManager http = Mockito.mock(IHttpManager.class);
        when(http.get(any(URI.class), any())).thenReturn(json);

        var request = new SearchRequest.Builder("token")
                .query("Kendrick")
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(http)
                .build();

        Search s = request.block();
        testDataIntegrity(s);
    }

    private void testDataIntegrity(Search s) {
        assertNotNull(s);
        assertFalse(s.getHits().isEmpty());
        assertEquals("song", s.getHits().get(0).getType());
        assertEquals(10359264, s.getHits().get(0).getResult().getId());

    }
}
