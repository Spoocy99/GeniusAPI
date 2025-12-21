package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.data.referents.ReferentsObject;
import dev.spoocy.genius.model.Referents;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetReferentsRequestTest implements ITest<Referents> {

    @Override
    public String getMockDataFile() {
        return "get_referents.json";
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        Referents r = ReferentsObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(r);
    }

    @Test
    void testRequest() throws Exception {
        var request = new GetReferentsRequest.Builder(GENIUS_CLIENT)
                .web_page_id(10347)
                .per_page(2)
                .page(1)
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(mockHttpManager())
                .build();

        Referents r = request.block();
        testDataIntegrity(r);
    }

    @Override
    public void testDataIntegrity(Referents r) {
        assertNotNull(r);
        assertFalse(r.getReferents().isEmpty());
        assertNotNull(r.getReferents().get(0).getAnnotations());
    }
}
