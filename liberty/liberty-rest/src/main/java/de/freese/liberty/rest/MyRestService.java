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
     * http://localhost:9080/liberty-rest/liberty/service/properties
     *
     * @return {@link JsonObject}
     */
    @GET
    @Path("properties")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getProperties()
    {
        System.out.printf("%s_MyRestService.getProperties%n", Thread.currentThread().getName());
        
        JsonObjectBuilder builder = Json.createObjectBuilder();

        System.getProperties().keySet().stream().sorted().forEach(key -> builder.add((String) key, System.getProperty((String) key)));

        // @formatter:off
//        System.getProperties()
//            .forEach((key, value) -> builder.add((String) key, (String) value))
//        ;
        // @formatter:on

        return builder.build();
    }
}
