import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.request.ArtistRequestBuilder;
import dev.spoocy.genius.core.request.LyricsRequestBuilder;
import dev.spoocy.genius.core.request.SearchRequestBuilder;
import dev.spoocy.genius.core.request.SongRequestBuilder;
import dev.spoocy.genius.data.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.CoreSubscriber;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AccessTokenTest {


    private GeniusClient client;

    @BeforeEach
    void setUp() {
        client = mock(GeniusClient.class);
    }

    @Test
    void testSearchSong() {
        var searchSongMock = mock(SongRequestBuilder.class);

        when(client.searchSong()).thenReturn(searchSongMock);
        when(searchSongMock.setId(anyString())).thenReturn(searchSongMock);
        when(searchSongMock.addFormat(any(TextFormat.class))).thenReturn(searchSongMock);
        doNothing().when(searchSongMock).subscribe((CoreSubscriber<? super Song>) any());

        assertDoesNotThrow(() -> {
            client.searchSong()
                    .setId("378195")
                    .addFormat(TextFormat.PLAIN)
                    .addFormat(TextFormat.HTML)
                    .subscribe(song -> {});
        });
    }

    @Test
    void testSearchArtist() {
        var searchArtistMock = mock(ArtistRequestBuilder.class);
        when(client.searchArtist()).thenReturn(searchArtistMock);
        when(searchArtistMock.setId(anyString())).thenReturn(searchArtistMock);
        when(searchArtistMock.addFormat(any(TextFormat.class))).thenReturn(searchArtistMock);
        doNothing().when(searchArtistMock).subscribe((CoreSubscriber<? super dev.spoocy.genius.data.Artist>) any());

        assertDoesNotThrow(() -> {
            client.searchArtist()
                    .setId("16775")
                    .addFormat(TextFormat.HTML)
                    .subscribe(artist -> {});
        });
    }

    @Test
    void testSearch() {
        var searchMock = mock(SearchRequestBuilder.class);
        when(client.search()).thenReturn(searchMock);
        when(searchMock.setQuery(anyString())).thenReturn(searchMock);
        when(searchMock.doOnError(any())).thenReturn(searchMock);
        doNothing().when(searchMock).subscribe((CoreSubscriber<? super dev.spoocy.genius.data.Search>) any());

        assertDoesNotThrow(() -> {
            client.search()
                    .setQuery("Kendrick Lamar")
                    .doOnError(e -> {})
                    .subscribe(search -> {});
        });
    }

    @Test
    void testLyrics() {
        var lyricsMock = mock(LyricsRequestBuilder.class);
        when(client.lyrics()).thenReturn(lyricsMock);
        when(lyricsMock.setSongUrl(anyString())).thenReturn(lyricsMock);
        when(lyricsMock.setSongName(anyString())).thenReturn(lyricsMock);
        when(lyricsMock.doOnError(any())).thenReturn(lyricsMock);
        doNothing().when(lyricsMock).subscribe((CoreSubscriber<? super dev.spoocy.genius.data.Lyrics>) any());

        assertDoesNotThrow(() -> {
            client.lyrics()
                    .setSongUrl("https://genius.com/Kendrick-lamar-humble-lyrics")
                    .setSongName("Kendrick Lamar humble")
                    .doOnError(e -> {})
                    .subscribe(lyrics -> {});
        });
    }
}
