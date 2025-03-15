// Created: 20.05.2018
package de.freese.liberty.rest;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.liberty.kryo.KryoReaderWriter;

/**
 * @author Thomas Freese
 */
@ApplicationScoped
@Path("service")
public class MyRestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyRestService.class);

    @POST
    @Path("kryo")
    @Consumes(KryoReaderWriter.KRYO_MEDIA_TYPE)
    @Produces(KryoReaderWriter.KRYO_MEDIA_TYPE)
    public List<String> kryo(final List<Long> longValues) {
        LOGGER.info("kryo: {}", longValues);

        if (longValues == null) {
            return List.of("null");
        }

        return longValues.stream().map(l -> l + "_value").toList();
    }

    /**
     * <a href="http://localhost:9080/liberty-rest/my-liberty/service/properties">localhost</a>
     */
    @GET
    @Path("properties")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject properties() {
        LOGGER.info("properties");

        final JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        System.getProperties().keySet().stream()
                .sorted()
                .map(String.class::cast)
                .forEach(key -> jsonObjectBuilder.add(key, System.getProperty(key)))
        ;

        // System.getProperties()
        //         .forEach((key, value) -> jsonObjectBuilder.add((String) key, (String) value))
        // ;

        return jsonObjectBuilder.build();
    }
}
