package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.data.webpages.WebPageObject;
import dev.spoocy.genius.model.WebPage;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetWebPagesLookupRequestTest implements ITest<WebPage> {

    @Override
    public String getMockDataFile() {
        return "get_webpages.json";
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        WebPage page = WebPageObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(page);
    }

    @Test
    void testRequest() throws Exception {
        var request = new GetWebPagesLookupRequest.Builder(GENIUS_CLIENT)
                .raw_annotatable_url("https://docs.genius.com")
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(mockHttpManager())
                .build();

        WebPage page = request.block();
        testDataIntegrity(page);
    }

    @Override
    public void testDataIntegrity(WebPage page) {
        assertNotNull(page);
        assertEquals(10347L, page.getId());
        assertEquals("Genius API", page.getTitle());
        assertEquals("https://genius.com/docs.genius.com", page.getUrl());
    }
}
