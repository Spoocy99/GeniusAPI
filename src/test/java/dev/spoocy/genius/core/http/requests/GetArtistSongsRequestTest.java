package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.data.artists.ArtistSongsObject;
import dev.spoocy.genius.core.http.IHttpManager;
import dev.spoocy.genius.model.ArtistSongs;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetArtistSongsRequestTest {

    private String mockJsonRequest() throws IOException {
        var is = this.getClass().getClassLoader().getResourceAsStream("get_artists_songs.json");
        assertNotNull(is, "Test resource not found: get_artists_songs.json");
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        ArtistSongs a = ArtistSongsObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(a);
    }

    @Test
    void testRequest() throws Exception {
        String json = mockJsonRequest();
        IHttpManager http = Mockito.mock(IHttpManager.class);
        when(http.get(any(URI.class), any())).thenReturn(json);

        var request = new GetArtistSongsRequest.Builder("token")
                .id(1421)
                .per_page(5)
                .page(1)
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(http)
                .build();

        ArtistSongs a = request.block();
        testDataIntegrity(a);
    }

    private void testDataIntegrity(ArtistSongs a) {
        assertNotNull(a);
        assertFalse(a.getSongs().isEmpty());
    }
}
