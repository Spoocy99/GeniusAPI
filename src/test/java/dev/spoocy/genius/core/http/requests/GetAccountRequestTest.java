package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.data.account.AccountObject;
import dev.spoocy.genius.core.http.IHttpManager;
import dev.spoocy.genius.model.Account;
import dev.spoocy.utils.config.Config;
import dev.spoocy.utils.config.documents.JsonConfig;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GetAccountRequestTest {

    private String mockJsonRequest() throws IOException {
        var is = this.getClass().getClassLoader().getResourceAsStream("get_account.json");
        assertNotNull(is, "Test resource not found: get_account.json");
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();

        Account account = AccountObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(account);
    }

    @Test
    void testRequest() throws Exception {
        String json = mockJsonRequest();
        IHttpManager http = Mockito.mock(IHttpManager.class);
        when(http.get(any(URI.class), any())).thenReturn(json);

        var request = new GetAccountRequest.Builder("token")
                .formats(TextFormat.PLAIN)
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(http)
                .build();

        var account = request.block();
        testDataIntegrity(account);
    }

    private void testDataIntegrity(Account account) {
        assertNotNull(account);
        assertEquals(94273412, account.getId());
        assertEquals(0, account.getIq());
        assertEquals("Test@email.de", account.getEmail());
        assertEquals("test_user", account.getName());
    }
}
