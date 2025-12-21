package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.data.artists.ArtistObject;
import dev.spoocy.genius.model.Artist;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetArtistRequestTest implements ITest<Artist> {

    @Override
    public String getMockDataFile() {
        return "get_artists.json";
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        Artist a = ArtistObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(a);
    }

    @Test
    void testRequest() throws Exception {
        var request = new GetArtistRequest.Builder(GENIUS_CLIENT)
                .id("1421")
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(mockHttpManager())
                .build();

        Artist a = request.block();
        testDataIntegrity(a);
    }

    @Override
    public void testDataIntegrity(Artist a) {
        assertNotNull(a);
        assertEquals("/artists/16775", a.getApiPath());
        assertEquals("Sia", a.getName());
        assertEquals(true, a.isVerified());
        assertNotNull(a.getCurrentUserMetaData());
    }
}
