// Created: 20.05.2018
package de.freese.liberty.rest;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;

import jakarta.annotation.Resource;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.UserTransaction;
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

import de.freese.liberty.interceptor.logging.MyLogging;
import de.freese.liberty.kryo.KryoReaderWriter;

/**
 * @author Thomas Freese
 */
@ApplicationScoped
@Path("service")
public class MyRestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyRestService.class);

    @Resource
    private UserTransaction ut;

    /**
     * <a href="http://localhost:9080/liberty-demo/my-app/service/exception">exception</a>
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
    @MyLogging
    public LocalDateTime exception() {
        throw new RuntimeException("Test MyExceptionMapper");
    }

    /**
     * <a href="http://localhost:9080/liberty-demo/my-app/service/jndi">jndi</a>
     */
    @GET
    @Path("jndi")
    @Produces(MediaType.APPLICATION_JSON)
    @MyLogging
    public Map<String, Object> getJndiTree() throws Exception {
        final List<String> names = Arrays.asList("", "java:global", "java:app", "java:module", "java:comp");

        final Map<String, Object> map = new TreeMap<>();
        final InitialContext initialContext = new InitialContext();

        for (String name : names) {
            final Context context = (Context) initialContext.lookup(name);
            map.put(name, dumpContextNameClassPair(context));
            // map.put(name, dumpContextBinding(context));
            context.close();
        }

        initialContext.close();

        return map;
    }

    @POST
    @Path("kryo")
    @Consumes(KryoReaderWriter.KRYO_MEDIA_TYPE)
    @Produces(KryoReaderWriter.KRYO_MEDIA_TYPE)
    @Operation(hidden = true)
    @MyLogging
    public List<String> kryo(final List<Long> longValues) {
        LOGGER.info("kryo: {}", longValues);

        if (longValues == null) {
            return List.of("null");
        }

        return longValues.stream().map(l -> l + "_value").toList();
    }

    /**
     * <a href="http://localhost:9080/liberty-demo/my-app/service/properties">localhost</a>
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
    @MyLogging
    @TransactionAttribute(TransactionAttributeType.NEVER)
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

    // @GET
    // @Produces({MediaType.TEXT_XML, MediaType.APPLICATION_JSON})
    // @Path("/{id:\\d+}")
    // // Nur Zahlen erlaubt.
    // @MyLogging
    // @TransactionAttribute(TransactionAttributeType.REQUIRED)
    // public Person getByID(@PathParam("id") final long id) {
    //     LOGGER.info("id = {}", id);
    //
    //     final List<Person> kunden = entityManager.createQuery("select p from Person p where p.id = :id", Person.class)
    //             .setParameter("id", id)
    //             .getResultList();
    //
    //     if (kunden.size() == 1) {
    //         return kunden.getFirst();
    //     }
    //
    //     return null;
    // }

    private Map<String, Object> dumpContextBinding(final Context ctx) throws Exception {
        final NamingEnumeration<Binding> enumeration = ctx.listBindings("");
        final Map<String, Object> map = new TreeMap<>();

        while (enumeration.hasMoreElements()) {
            final Binding binding = enumeration.next();
            final String name = binding.getName();

            Object tmp = null;

            try {
                tmp = binding.getObject();
            }
            catch (Exception ex) {
                tmp = binding.getClassName();
            }

            if (tmp instanceof Context subContext) {
                map.put(name, dumpContextBinding(subContext));
            }
            else {
                map.put(name, tmp.toString());
            }
        }

        enumeration.close();

        return map;
    }

    private Map<String, Object> dumpContextNameClassPair(final Context ctx) throws Exception {
        final NamingEnumeration<NameClassPair> enumeration = ctx.list("");
        final Map<String, Object> map = new TreeMap<>();

        while (enumeration.hasMoreElements()) {
            final NameClassPair pair = enumeration.next();
            final String name = pair.getName();
            final String className = pair.getClassName();

            boolean isSubContext = false;

            try {
                isSubContext = MethodHandles.publicLookup().findClass(className).isAssignableFrom(Context.class);
                // isSubContext = Class.forName(className).isAssignableFrom(Context.class);
            }
            catch (Exception ex) {
                // Ignore
            }

            if (isSubContext) {
                final Context subContext = (Context) ctx.lookup(name);

                map.put(name, dumpContextNameClassPair(subContext));
            }
            else {
                try {
                    map.put(name, ctx.lookup(name).toString());
                }
                catch (Exception ex) {
                    map.put(name, className);
                }
            }
        }

        enumeration.close();

        return map;
    }
}
