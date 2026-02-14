package dev.spoocy.genius.core.auth;

import dev.spoocy.genius.core.http.requests.ITest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccessTokenRequestTest implements ITest<GeniusAccessToken> {

    @Override
    public String getMockDataFile() {
        return "get_oauth_token.json";
    }

    @Test
    void testRequest() throws Exception {
        var request = new AccessTokenRequest.Builder("client_id", "client_secret")
                .code("authorization_code_123")
                .scope(Scope.ME, Scope.VOTE)
                .redirectUri("https://example.com/callback")
                .httpManager(mockHttpManager())
                .build();

        GeniusAccessToken r = request.block();
        assertNotNull(r);
        testDataIntegrity(r);
    }

    @Override
    public void testDataIntegrity(GeniusAccessToken data) throws Exception {
        assertEquals("ACCESS_TOKEN_123", data.accessToken());
        assertEquals(2, data.scopes().length);
        assertEquals("Bearer", data.tokenType());
    }

}

