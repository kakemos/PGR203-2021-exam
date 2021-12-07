package no.kristiania.http;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HttpClientTest {

    @Test
    public void shouldReturnStatusCode() throws IOException {
        assertEquals(200, new HttpClient("httpbin.org", 80, "/html").getStatusCode());
        assertEquals(404, new HttpClient("httpbin.org", 80, "/no-such-page").getStatusCode());
    }

    @Test
    void shouldReadResponseHeaders() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals("text/html; charset=utf-8", client.getHeader("Content-Type"));
    }

    @Test
    void shouldReadBody() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertTrue("Expected HTML: " + client.getMessageBody(), client.getMessageBody().startsWith("<!DOCTYPE html>"));
    }
}
