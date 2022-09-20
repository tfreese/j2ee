// Created: 20.05.2018
package de.freese.liberty.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Map;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.junit.jupiter.api.Test;

/**
 * @author Thomas Freese
 */
class EndpointTest
{
    /**
     * http://localhost:9080/liberty-rest/liberty/service/properties
     */
    @Test
    void testGetProperties()
    {
        String url = "http://localhost:9080/liberty-rest/liberty/service/properties";

        Client client = ClientBuilder.newClient().register(JacksonJaxbJsonProvider.class);

        WebTarget target = client.target(url);

        try (Response response = target.request().get())
        {
            assertEquals(200, response.getStatus(), "Incorrect response code from " + url);

            Map<String, String> props = response.readEntity(Map.class);

            assertFalse(props.isEmpty());
            assertEquals(System.getProperty("os.name"), props.get("os.name"), "The system property for the local and remote JVM should match");
        }
        finally
        {
            client.close();
        }
    }
}
