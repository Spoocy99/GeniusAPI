package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.TextFormat;
import dev.spoocy.genius.core.data.account.AccountObject;
import dev.spoocy.genius.model.Account;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class GetAccountRequestTest implements ITest<Account> {

    @Override
    public String getMockDataFile() {
        return "get_account.json";
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        Account account = AccountObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(account);
    }

    @Test
    void testRequest() throws Exception {
        var request = new GetAccountRequest.Builder(GENIUS_CLIENT)
                .formats(TextFormat.PLAIN)
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(mockHttpManager())
                .build();

        var account = request.block();
        testDataIntegrity(account);
    }

    @Override
    public void testDataIntegrity(Account account) {
        assertNotNull(account);
        assertEquals(94273412, account.getId());
        assertEquals(0, account.getIq());
        assertEquals("Test@email.de", account.getEmail());
        assertEquals("test_user", account.getName());
    }
}
