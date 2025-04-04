// Created: 04 Apr. 2025
package de.freese.liberty;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * @author Thomas Freese
 */
@Provider
public final class MyExceptionMapper implements ExceptionMapper<Exception> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(final Exception exception) {
        String stackTrace = null;

        try (StringWriter stringWriter = new StringWriter()) {
            exception.printStackTrace(new PrintWriter(stringWriter, true));

            stackTrace = stringWriter.toString();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        final JsonObject jsonObject = Json.createObjectBuilder()
                .add("host", uriInfo.getAbsolutePath().getHost())
                .add("resource", uriInfo.getAbsolutePath().getPath())
                .add("title", exception.getMessage())
                .add("class", exception.getClass().getSimpleName())
                .add("stacktrace", stackTrace)
                .build();

        return Response.status(Response.Status.BAD_REQUEST).entity(jsonObject).build();
    }
}
