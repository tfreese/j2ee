package de.freese.liberty.kryo;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ext.Provider;

/**
 * @author Thomas Freese
 * @since 22.03.2025
 */
@Provider // Must bei part of the WAR, and not in a Dependency.
@Consumes(KryoReaderWriter.KRYO_MEDIA_TYPE)
@Produces(KryoReaderWriter.KRYO_MEDIA_TYPE)
public class KryoProvider extends KryoContextResolver {
}
