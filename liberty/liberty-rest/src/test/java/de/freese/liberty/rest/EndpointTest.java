// Created: 20.05.2018
package de.freese.liberty.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.junit.jupiter.api.Test;

/**
 * @author Thomas Freese
 */
class EndpointTest {
    /**
     * http://localhost:9080/liberty-rest/liberty/service/properties
     */
    @Test
    void testGetProperties() throws Exception {
        String url = "http://localhost:9080/liberty-rest/my-liberty/service/properties";

        try (Client client = ClientBuilder.newClient().register(JacksonJaxbJsonProvider.class)) {
            WebTarget webTarget = client.target(url);

            try (Response response = webTarget.request().get()) {
                assertEquals(200, response.getStatus(), "Incorrect response code from " + url);

                String jsonValue = response.readEntity(String.class);
                assertNotNull(jsonValue);

                System.out.println(jsonValue);

                TypeReference<HashMap<String, Object>> typeReference = new TypeReference<>() {
                };
                HashMap<String, Object> map = new ObjectMapper().readValue(jsonValue, typeReference);

                assertNotNull(map);
                assertFalse(map.isEmpty());
                assertEquals(System.getProperty("os.name"), map.get("os.name"), "The system property for the local and remote 'os.name' should match");
            }
        }
    }
}
