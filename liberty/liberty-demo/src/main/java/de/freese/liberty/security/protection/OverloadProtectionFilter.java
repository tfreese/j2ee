package de.freese.liberty.security.protection;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Provider
@OverloadProtected
@Priority(Priorities.USER)
public class OverloadProtectionFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(OverloadProtectionFilter.class);

    @Inject
    private MemoryGuard memoryGuard;

    @Override
    public void filter(final ContainerRequestContext ctx) {
        if (memoryGuard.isOverloaded()) {
            LOGGER.warn("Request rejected due to overload: {}", ctx.getUriInfo().getRequestUri());

            ctx.abortWith(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"error\":\"server_overloaded\",\"reason\":\"heap_pressure\"}")
                    .build()
            );
        }
    }
}