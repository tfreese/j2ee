// Created: 23.05.2013
package de.freese.j2ee.client;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.j2ee.model.Kunde;
import de.freese.j2ee.rest.KundenService;

/**
 * @author Thomas Freese
 */
public final class EjbClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(EjbClient.class);

    public static void main(final String[] args) throws Exception {
        // final Properties props = new Properties();
        // props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        // props.setProperty(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
        // props.setProperty(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
        // props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
        // props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

        // glassfish/lib/appserv-rt.jar im ClassPath braucht keine Konfig für lokale Maschine.
        final Context ctx = new InitialContext();

        // KundenService service = (KundenService) ctx.lookup("java:global/de.freese.j2ee/RestService");
        final KundenService service = (KundenService) ctx.lookup(KundenService.class.getName());
        final List<Kunde> kunden = service.getData();

        for (Kunde kunde : kunden) {
            LOGGER.info("{}", kunde);
        }
    }

    private EjbClient() {
        super();
    }
}
