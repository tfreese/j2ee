// Created: 26 Juli 2024
package de.freese.j2ee.liberty.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Thomas Freese
 */
class TestEndpoint {
    private static URI uriBase;

    @BeforeAll
    static void init() {
        final String port = System.getProperty("http.port");
        final String context = System.getProperty("context.root");
        uriBase = URI.create("http://localhost:%s/%s".formatted(port, context));
    }

    @Test
    void testRestService() throws Exception {
        final URI uriRestService = URI.create(uriBase.toString() + "/sysdate");

        try (HttpClient client = HttpClient.newBuilder().build()) {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(uriRestService)
                    .GET()
                    .build();

            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode(), "Incorrect response code from " + uriRestService);
            assertNotNull(response.body());

            final String remoteSysDate = response.body().replace("\"", "");

            // 2025-06-21T10:30:21.728828
            final LocalDateTime remoteDateTime = LocalDateTime.parse(remoteSysDate);
            // final LocalDateTime remoteDateTime =LocalDateTime.parse(remoteSysDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));

            final LocalDateTime now = LocalDateTime.now();
            assertEquals(now.getYear(), remoteDateTime.getYear());
            assertEquals(now.getMonth(), remoteDateTime.getMonth());
            assertEquals(now.getDayOfWeek(), remoteDateTime.getDayOfWeek());
            assertEquals(now.getHour(), remoteDateTime.getHour());
            assertEquals(now.getMinute(), remoteDateTime.getMinute());
        }
    }
}
