package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.data.referents.ReferentsObject;
import dev.spoocy.genius.core.http.IHttpManager;
import dev.spoocy.genius.model.Referents;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetReferentsRequestTest {

    private String mockJsonRequest() throws IOException {
        var is = this.getClass().getClassLoader().getResourceAsStream("get_referents.json");
        assertNotNull(is, "Test resource not found: get_referents.json");
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        Referents r = ReferentsObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(r);
    }

    @Test
    void testRequest() throws Exception {
        String json = mockJsonRequest();
        IHttpManager http = Mockito.mock(IHttpManager.class);
        when(http.get(any(URI.class), any())).thenReturn(json);

        var request = new GetReferentsRequest.Builder("token")
                .web_page_id(10347)
                .per_page(2)
                .page(1)
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(http)
                .build();

        Referents r = request.block();
        testDataIntegrity(r);
    }

    private void testDataIntegrity(Referents r) {
        assertNotNull(r);
        assertFalse(r.getReferents().isEmpty());
        assertNotNull(r.getReferents().get(0).getAnnotations());
    }
}
