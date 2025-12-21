package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.data.songs.SongObject;
import dev.spoocy.genius.model.Song;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetSongRequestTest implements ITest<Song> {

    @Override
    public String getMockDataFile() {
        return "get_songs.json";
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        Song s = SongObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(s);
    }

    @Test
    void testRequest() throws Exception {
        var request = new GetSongRequest.Builder(GENIUS_CLIENT)
                .id(378195)
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(mockHttpManager())
                .build();

        Song s = request.block();
        testDataIntegrity(s);
    }

    @Override
    public void testDataIntegrity(Song s) {
        assertNotNull(s);
        assertEquals(32, s.getAnnotationCount());
        assertEquals(378195, s.getId());
        assertEquals("Chandelier", s.getTitle());
    }
}
