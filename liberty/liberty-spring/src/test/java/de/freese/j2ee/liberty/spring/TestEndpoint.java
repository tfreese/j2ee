// Created: 26 Juli 2024
package de.freese.j2ee.liberty.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Thomas Freese
 */
class TestEndpoint {
    private static URI uri;

    @BeforeAll
    public static void init() {
        final String port = System.getProperty("http.port");
        final String context = System.getProperty("context.root");
        uri = URI.create("http://localhost:%s/%s/sysdate".formatted(port, context));
        System.out.printf("URL: %s%n", uri);
    }

    @Test
    void testServlet() throws Exception {
        try (HttpClient httpClient = HttpClient.newBuilder().build()) {
            final HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, httpResponse.statusCode(), "Incorrect response code from " + uri);
            assertNotNull(httpResponse.body());
        }
    }
}
