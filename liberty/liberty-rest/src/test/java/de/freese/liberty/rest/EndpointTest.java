// Created: 20.05.2018
package de.freese.liberty.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Thomas Freese
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class EndpointTest
{
    /**
     * http://localhost:9080/rest/liberty/service/properties
     */
    @Test
    void testGetProperties()
    {
        String port = "9080";
        String war = "liberty-rest";
        String base = "liberty";
        String url = "http://localhost:" + port + "/" + war + "/" + base;

        Client client = ClientBuilder.newClient().register(JsrJsonpProvider.class);

        WebTarget target = client.target(url + "/service/properties");

        try (Response response = target.request().get())
        {
            assertEquals(200, response.getStatus(), "Incorrect response code from " + url);

            JsonObject obj = response.readEntity(JsonObject.class);

            assertEquals(System.getProperty("os.name"), obj.getString("os.name"), "The system property for the local and remote JVM should match");
        }
    }
}
