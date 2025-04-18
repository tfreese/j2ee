// Created: 20.05.2018
package de.freese.liberty.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
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
    @Tag(name = "Exception")
    @Operation(summary = "Throws a RuntimeException.")
    @APIResponse(responseCode = "200", description = "Will never happen")
    @APIResponse(responseCode = "400", description = "Server-Time",
            content = {
                    @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = LocalDateTime.class))
                    // @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(defaultValue = "false"))
            }
    )
    public LocalDateTime exception() {
        throw new RuntimeException("Test MyExceptionMapper");
    }

    @POST
    @Path("kryo")
    @Consumes(KryoReaderWriter.KRYO_MEDIA_TYPE)
    @Produces(KryoReaderWriter.KRYO_MEDIA_TYPE)
    @Operation(hidden = true)
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
    @Tag(name = "Server")
    @Operation(summary = "Server Properties.")
    @APIResponse(responseCode = "200", description = "Server Properties",
            content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Map.class))
            }
    )
    public Map<String, String> properties(@Parameter(description = "Parameter", required = false, schema = @Schema(implementation = String.class),
            examples = {@ExampleObject(name = "1", value = "1 Value")}) final String parameter) {
        LOGGER.info("properties");

        return System.getProperties().keySet().stream()
                .map(String.class::cast)
                .collect(Collectors.toMap(Function.identity(), System::getProperty, (a, b) -> a, TreeMap::new));

        // final JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        //
        // System.getProperties().keySet().stream()
        //         .sorted()
        //         .map(String.class::cast)
        //         .forEach(key -> jsonObjectBuilder.add(key, System.getProperty(key)))
        // ;
        //
        // return jsonObjectBuilder.build();
    }
}
