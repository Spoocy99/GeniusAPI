package dev.spoocy.genius.core.auth;

import dev.spoocy.genius.core.http.requests.ITest;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class AuthorizationCodeUriRequestTest implements ITest<URI> {

    @Override
    public String getMockDataFile() {
        return "get_oauth_token.json";
    }

    @Test
    void testRequest() throws Exception {
        var request = new AuthorizationCodeUriRequest.Builder()
                .httpManager(mockHttpManager())
                .clientId("client_id")
                .redirectUri("https://example.com/callback")
                .scope(Scope.ME, Scope.VOTE)
                .state("some_state_value")
                .build();

        URI r = request.block();
        assertNotNull(r);
        testDataIntegrity(r);
    }

    @Override
    public void testDataIntegrity(URI data) throws Exception {
        assertEquals("https", data.getScheme());
        assertEquals("api.genius.com", data.getHost());
        assertEquals(443, data.getPort());
        assertEquals("/oauth/authorize", data.getPath());

        var query = data.getQuery();
        assertNotNull(query);

        var params = query.split("&");
        Set<String> paramSet = Set.of(params);

        assertEquals(5, params.length);
        assert(paramSet.contains("response_type=code"));
        assert(paramSet.contains("client_id=client_id"));
        assert(paramSet.contains("state=some_state_value"));
        assert(paramSet.contains("scope=me vote"));
        assert(paramSet.contains("redirect_uri=https://example.com/callback"));
    }

}
