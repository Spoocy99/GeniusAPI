package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.GeniusClient;
import dev.spoocy.genius.core.http.NettyHttpManager;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ITest<T> {

    GeniusClient GENIUS_CLIENT = GeniusClient.builder()
            .clientId("TEST_ID")
            .clientSecret("TEST_SECRET")
            .accessToken("TEST_ACCESS")
            .build();

    default String mockJsonRequest() throws IOException {
        String fileName = getMockDataFile();

        var is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        assertNotNull(is, "Test resource not found: " + fileName);
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    default NettyHttpManager mockHttpManager() throws IOException {
        String json = mockJsonRequest();
        NettyHttpManager http = Mockito.mock(NettyHttpManager.class);

        when(http.get(any(URI.class), any()))
                .thenReturn(Mono.just(json));

        when(http.post(any(URI.class), any(), any()))
                .thenReturn(Mono.just(json));

        when(http.put(any(URI.class), any(), any()))
                .thenReturn(Mono.just(json));

        when(http.delete(any(URI.class), any()))
                .thenReturn(Mono.just(json));

        return http;
    }

    String getMockDataFile();

    void testDataIntegrity(T data) throws Exception;

}
