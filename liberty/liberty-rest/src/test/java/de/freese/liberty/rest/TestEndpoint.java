// Created: 20.05.2018
package de.freese.liberty.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;

/**
 * @author Thomas Freese
 */
class TestEndpoint {
    /**
     * http://localhost:9080/liberty-rest/my-liberty/service/properties
     */
    @Test
    void testGetProperties() throws Exception {
        final String port = System.getProperty("http.port");
        final String context = System.getProperty("context.root");
        final URI uri = URI.create("http://localhost:%s/liberty-rest/my-liberty/service/properties".formatted(port, context));

        try (HttpClient httpClient = HttpClient.newBuilder().build()) {
            final HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, httpResponse.statusCode(), "Incorrect response code from " + uri);
            assertNotNull(httpResponse.body());
        }

        // try (Client client = ClientBuilder.newClient().register(JacksonJaxbJsonProvider.class)) {
        //     final WebTarget webTarget = client.target(uri);
        //
        //     try (Response response = webTarget.request().get()) {
        //         assertEquals(200, response.getStatus(), "Incorrect response code from " + uri);
        //
        //         final String jsonValue = response.readEntity(String.class);
        //         assertNotNull(jsonValue);
        //
        //         System.out.println(jsonValue);
        //
        //         final TypeReference<HashMap<String, Object>> typeReference = new TypeReference<>() {
        //         };
        //         final HashMap<String, Object> map = new ObjectMapper().readValue(jsonValue, typeReference);
        //
        //         assertNotNull(map);
        //         assertFalse(map.isEmpty());
        //         assertEquals(System.getProperty("os.name"), map.get("os.name"), "The system property for the local and remote 'os.name' should match");
        //     }
        // }
    }
}
