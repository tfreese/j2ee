// Created: 22 März 2025
package de.freese.liberty.kryo;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ext.Provider;

/**
 * @author Thomas Freese
 */
@Provider
@Consumes(KryoReaderWriter.KRYO_MEDIA_TYPE)
@Produces(KryoReaderWriter.KRYO_MEDIA_TYPE)
public class KryoProvider extends KryoContextResolver {
}
