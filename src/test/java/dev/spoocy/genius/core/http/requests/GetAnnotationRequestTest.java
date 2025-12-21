package dev.spoocy.genius.core.http.requests;

import dev.spoocy.genius.core.http.IHttpManager;
import dev.spoocy.genius.core.data.annotations.AnnotationDataObject;
import dev.spoocy.genius.model.AnnotationData;
import org.apache.hc.core5.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetAnnotationRequestTest {

    private String mockJsonRequest() throws IOException {
        var is = this.getClass().getClassLoader().getResourceAsStream("get_annotations.json");
        assertNotNull(is, "Test resource not found: get_annotations.json");
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Test
    void testParser() throws Exception {
        String json = mockJsonRequest();
        AnnotationData data = AnnotationDataObject.Parser.INSTANCE.parseResponse(json);
        testDataIntegrity(data);
    }

    @Test
    void testRequest() throws Exception {
        String json = mockJsonRequest();
        IHttpManager http = Mockito.mock(IHttpManager.class);
        when(http.get(any(URI.class), any())).thenReturn(json);

        var request = new GetAnnotationRequest.Builder("token")
                .id(10225840)
                .contentType(ContentType.APPLICATION_JSON)
                .httpManager(http)
                .build();

        AnnotationData data = request.block();
        testDataIntegrity(data);
    }

    private void testDataIntegrity(AnnotationData data) {
        assertNotNull(data);
        assertEquals(10225840, data.getAnnotation().getId());
        assertEquals(10225839, data.getReferent().getId());
    }
}
