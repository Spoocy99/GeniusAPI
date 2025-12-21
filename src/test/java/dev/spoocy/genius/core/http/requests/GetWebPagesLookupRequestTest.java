package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.data.webpages.WebPageObject;
import dev.spoocy.genius.core.http.IHttpManager;
import dev.spoocy.genius.model.WebPage;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetWebPagesLookupRequestTest {

    private String mockJsonRequest() throws IOException {
        var is = this.getClass().getClassLoader().getResourceAsStream("get_webpages.json");
        assertNotNull(is, "Test resource not found: get_webpages.json");
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        WebPage page = WebPageObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(page);
    }

    @Test
    void testRequest() throws Exception {
        String json = mockJsonRequest();
        IHttpManager http = Mockito.mock(IHttpManager.class);
        when(http.get(any(URI.class), any())).thenReturn(json);

        var request = new GetWebPagesLookupRequest.Builder("token")
                .raw_annotatable_url("https://docs.genius.com")
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(http)
                .build();

        WebPage page = request.block();
        testDataIntegrity(page);
    }

    private void testDataIntegrity(WebPage page) {
        assertNotNull(page);
        assertEquals(10347L, page.getId());
        assertEquals("Genius API", page.getTitle());
        assertEquals("https://genius.com/docs.genius.com", page.getUrl());
    }
}
