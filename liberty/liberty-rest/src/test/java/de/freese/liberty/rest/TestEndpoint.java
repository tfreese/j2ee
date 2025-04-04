// Created: 20.05.2018
package de.freese.liberty.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.freese.liberty.kryo.KryoProvider;
import de.freese.liberty.kryo.KryoReaderWriter;

/**
 * @author Thomas Freese
 */
class TestEndpoint {
    private static String serverUrl;

    @BeforeAll
    static void beforeAll() {
        final String port = System.getProperty("http.port");
        final String context = System.getProperty("context.root");
        serverUrl = "http://localhost:%s/%s/my-liberty/service".formatted(port, context);
    }

    /**
     * <a href="http://localhost:9080/liberty-rest/my-liberty/service/properties">localhost</a>
     */
    @Test
    void testException() throws Exception {
        final URI uri = URI.create(serverUrl + "/exception");

        try (HttpClient httpClient = HttpClient.newBuilder().build()) {
            final HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, httpResponse.statusCode(), "Incorrect response code from " + uri);
            assertNotNull(httpResponse.body());
            assertTrue(httpResponse.body().contains("Test MyExceptionMapper"));
        }
    }

    /**
     * <a href="http://localhost:9080/liberty-rest/my-liberty/service/kryo">localhost</a>
     */
    @Test
    void testKryo() throws Exception {
        final URI uri = URI.create(serverUrl + "/kryo");

        final KryoProvider kryoProvider = new KryoProvider();

        byte[] bytes = null;

        try (Output output = new Output(16_384)) {
            kryoProvider.getKryo().writeClassAndObject(output, List.of(1L, 2L, 3L));
            bytes = output.toBytes();
        }

        try (HttpClient httpClient = HttpClient.newBuilder().build()) {
            final HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", KryoReaderWriter.KRYO_MEDIA_TYPE)
                    .header("Accept", KryoReaderWriter.KRYO_MEDIA_TYPE)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(bytes))
                    .build();

            final HttpResponse<InputStream> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());

            assertEquals(HttpURLConnection.HTTP_OK, httpResponse.statusCode(), "Incorrect response code from " + uri);

            try (InputStream inputStream = httpResponse.body();
                 Input input = new Input(inputStream)) {
                assertTrue(inputStream.available() > 0, "Empty content");

                final List<String> stringValues = (List) kryoProvider.getKryo().readClassAndObject(input);

                // Needs Registration in Kryo.
                // final List<String> stringValues = kryoProvider.getKryo().readObjectOrNull(input, ArrayList.class);

                assertNotNull(stringValues);
                assertEquals(List.of("1_value", "2_value", "3_value"), stringValues, "Incorrect response from " + uri);
            }
        }
    }

    /**
     * <a href="http://localhost:9080/liberty-rest/my-liberty/service/properties">localhost</a>
     */
    @Test
    void testProperties() throws Exception {
        final URI uri = URI.create(serverUrl + "/properties");

        try (HttpClient httpClient = HttpClient.newBuilder().build()) {
            final HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            assertEquals(HttpURLConnection.HTTP_OK, httpResponse.statusCode(), "Incorrect response code from " + uri);
            assertNotNull(httpResponse.body());
        }

        // try (Client client = ClientBuilder.newClient().register(JacksonJaxbJsonProvider.class)) {
        //     final WebTarget webTarget = client.target(uri);
        //
        //     try (Response response = webTarget.request().get()) {
        //         assertEquals(HttpURLConnection.HTTP_OK, response.getStatus(), "Incorrect response code from " + uri);
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
