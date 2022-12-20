// Created: 20.05.2018
package de.freese.liberty.rest;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * @author Thomas Freese
 */
@Path("service")
public class MyRestService
{
    /**
     * http://localhost:9080/liberty-rest/my-liberty/service/properties
     */
    @GET
    @Path("properties")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getProperties()
    {
        System.out.printf("%s_MyRestService.getProperties%n", Thread.currentThread().getName());

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        // @formatter:off
        System.getProperties().keySet().stream()
                .sorted()
                .map(String.class::cast)
                .forEach(key -> jsonObjectBuilder.add(key, System.getProperty(key)))
        ;
        // @formatter:on

        // @formatter:off
//        System.getProperties()
//            .forEach((key, value) -> jsonObjectBuilder.add((String) key, (String) value))
//        ;
        // @formatter:on

        return jsonObjectBuilder.build();
    }
}
