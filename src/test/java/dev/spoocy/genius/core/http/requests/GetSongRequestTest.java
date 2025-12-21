package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.data.songs.SongObject;
import dev.spoocy.genius.core.http.IHttpManager;
import dev.spoocy.genius.model.Song;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetSongRequestTest {

    private String mockJsonRequest() throws IOException {
        var is = this.getClass().getClassLoader().getResourceAsStream("get_songs.json");
        assertNotNull(is, "Test resource not found: get_songs.json");
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        Song s = SongObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(s);
    }

    @Test
    void testRequest() throws Exception {
        String json = mockJsonRequest();
        IHttpManager http = Mockito.mock(IHttpManager.class);
        when(http.get(any(URI.class), any())).thenReturn(json);

        var request = new GetSongRequest.Builder("token")
                .id(378195)
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(http)
                .build();

        Song s = request.block();
        testDataIntegrity(s);
    }

    private void testDataIntegrity(Song s) {
        assertNotNull(s);
        assertEquals(32, s.getAnnotationCount());
        assertEquals(378195, s.getId());
        assertEquals("Chandelier", s.getTitle());
    }
}
