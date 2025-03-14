// Created: 20.05.2018
package de.freese.liberty.rest;

import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import de.freese.liberty.kryo.KryoReaderWriter;

/**
 * @author Thomas Freese
 */
@Path("service")
public class MyRestService {
    @POST
    @Path("kryo")
    @Consumes(KryoReaderWriter.KRYO_MEDIA_TYPE)
    @Produces(KryoReaderWriter.KRYO_MEDIA_TYPE)
    public List<String> kryo(final List<Long> longValues) {
        System.out.printf("%s_MyRestService.withKryo%n", Thread.currentThread().getName());

        return longValues.stream().map(l -> l + "_value").toList();

        // byte[] bytes = null;
        //
        // try (Output output = new Output(16_384)) {
        //     new KryoProvider().getKryo().writeClassAndObject(output, longValues.stream().map(l -> l + "_value").toList());
        //     bytes = output.toBytes();
        // }
        //
        // return bytes;
    }

    /**
     * <a href="http://localhost:9080/liberty-rest/my-liberty/service/properties">localhost</a>
     */
    @GET
    @Path("properties")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject properties() {
        System.out.printf("%s_MyRestService.getProperties%n", Thread.currentThread().getName());

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
