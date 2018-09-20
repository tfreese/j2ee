/**
 * Created: 20.05.2018
 */

package de.freese.liberty.rest;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Thomas Freese
 */
// @Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EndpointTest
{
    /**
     * Erstellt ein neues {@link EndpointTest} Object.
     */
    public EndpointTest()
    {
        super();
    }

    /**
     * http://localhost:9080/rest/liberty/service/properties
     */
    @Test
    public void testGetProperties()
    {
        String port = "9080";
        String war = "liberty-rest";
        String base = "liberty";
        String url = "http://localhost:" + port + "/" + war + "/" + base + "/";

        Client client = ClientBuilder.newClient().register(JsrJsonpProvider.class);

        WebTarget target = client.target(url + "service/properties");

        try (Response response = target.request().get())
        {
            Assert.assertEquals("Incorrect response code from " + url, 200, response.getStatus());

            JsonObject obj = response.readEntity(JsonObject.class);

            Assert.assertEquals("The system property for the local and remote JVM should match", System.getProperty("os.name"), obj.getString("os.name"));
        }
    }
}
