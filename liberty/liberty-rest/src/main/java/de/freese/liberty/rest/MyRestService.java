// Created: 20.05.2018
package de.freese.liberty.rest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Thomas Freese
 */
@Path("service")
public class MyRestService
{
    /**
     * http://localhost:9080/liberty-rest/liberty/service/properties
     *
     * @return {@link JsonObject}
     */
    @GET
    @Path("properties")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getProperties()
    {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        // @formatter:off
        System.getProperties()
//            .entrySet()
//            .stream()
//            .forEach(entry -> builder.add((String) entry.getKey(), (String) entry.getValue()));
            .forEach((key, value) -> builder.add((String) key, (String) value));
        // @formatter:on

        return builder.build();
    }
}
