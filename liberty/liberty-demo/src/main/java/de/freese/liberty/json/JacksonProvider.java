// Created: 22 März 2025
package de.freese.liberty.json;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;

/**
 * @author Thomas Freese
 */
@Provider // Must bei part of the WAR, and not in a Dependency.
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JacksonProvider extends JacksonContextResolver {
}

// An alternative is JacksonJsonProvider from com.fasterxml.jackson.jakarta.rs:jackson-jakarta-rs-json-provider.
// public class JacksonProvider extends JacksonJsonProvider {
//     public JacksonProvider() {
//         super(new JacksonContextResolver().getObjectMapper());
//     }
// }
