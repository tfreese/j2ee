// Created: 14 März 2025
package de.freese.j2ee.interceptor.compress;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;

/**
 * <pre>{@code
 * @POST
 * @Path("METHOD")
 * @Consumes(APPLICATION_OCTET_STREAM)
 * @Produces(APPLICATION_OCTET_STREAM)
 * @Compress
 * }</pre>
 *
 * @author Thomas Freese
 */
@Provider
@Compress
public final class GZIPWriterInterceptor implements WriterInterceptor {
    @Override
    public void aroundWriteTo(final WriterInterceptorContext context) throws IOException, WebApplicationException {
        context.getHeaders().putSingle("Content-Encoding", "gzip");
        context.setOutputStream(new GZIPOutputStream(context.getOutputStream()));
        context.proceed();
    }
}
