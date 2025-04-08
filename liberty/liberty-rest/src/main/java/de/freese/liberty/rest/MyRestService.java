// Created: 20.05.2018
package de.freese.liberty.rest;

import java.time.LocalDateTime;
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

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
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

    /**
     * <a href="http://localhost:9080/liberty-rest/my-liberty/service/exception">exception</a>
     */
    @GET
    @Path("exception")
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "exception")
    @Operation(summary = "Wirft eine RuntimeException.")
    @APIResponse(responseCode = "400", description = "Server-Status",
            content = {
                    @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(defaultValue = "false"))
            }
    )
    public LocalDateTime exception() {
        throw new RuntimeException("Test MyExceptionMapper");
    }

    @POST
    @Path("kryo")
    @Consumes(KryoReaderWriter.KRYO_MEDIA_TYPE)
    @Produces(KryoReaderWriter.KRYO_MEDIA_TYPE)
    @Tag(name = "kryo")
    @Operation(summary = "Kryo Serialisierung.")
    @APIResponse(responseCode = "200", description = "Kryo-Values",
            content = {
                    @Content(mediaType = KryoReaderWriter.KRYO_MEDIA_TYPE, schema = @Schema(implementation = String.class))
            }
    )
    public List<String> kryo(
            @Parameter(description = "Long Values ", required = true, schema = @Schema(implementation = Long.class),
                    examples = {@ExampleObject(name = "1", value = "1 Value"), @ExampleObject(name = "2", value = "2 Value")}) final List<Long> longValues) {
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
    @Operation(hidden = true)
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
