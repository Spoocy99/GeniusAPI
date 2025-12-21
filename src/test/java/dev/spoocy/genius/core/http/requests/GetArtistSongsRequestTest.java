package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.data.artists.ArtistSongsObject;
import dev.spoocy.genius.model.ArtistSongs;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetArtistSongsRequestTest implements ITest<ArtistSongs> {

    @Override
    public String getMockDataFile() {
        return "get_artists_songs.json";
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        ArtistSongs a = ArtistSongsObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(a);
    }

    @Test
    void testRequest() throws Exception {
        var request = new GetArtistSongsRequest.Builder(GENIUS_CLIENT)
                .id(1421)
                .per_page(5)
                .page(1)
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(mockHttpManager())
                .build();

        ArtistSongs a = request.block();
        testDataIntegrity(a);
    }

    @Override
    public void testDataIntegrity(ArtistSongs a) {
        assertNotNull(a);
        assertFalse(a.getSongs().isEmpty());
    }
}
