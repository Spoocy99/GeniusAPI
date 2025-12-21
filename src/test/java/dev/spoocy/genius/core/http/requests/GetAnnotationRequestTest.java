package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.data.annotations.AnnotationDataObject;
import dev.spoocy.genius.model.AnnotationData;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetAnnotationRequestTest implements ITest<AnnotationData> {

    @Override
    public String getMockDataFile() {
        return "get_annotations.json";
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        AnnotationData data = AnnotationDataObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(data);
    }

    @Test
    void testRequest() throws Exception {
        var request = new GetAnnotationRequest.Builder(GENIUS_CLIENT)
                .id(10225840)
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(mockHttpManager())
                .build();

        AnnotationData data = request.block();
        testDataIntegrity(data);
    }

    @Override
    public void testDataIntegrity(AnnotationData data) {
        assertNotNull(data);
        assertEquals(10225840, data.getAnnotation().getId());
        assertEquals(10225839, data.getReferent().getId());
    }
}
