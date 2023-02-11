package de.freese.agentportal.client.ejb;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

/**
 * @author Thomas Freese
 */
public final class ServiceLocator {
    /**
     *
     */
    private static Context context;

    /**
     * @throws NamingException Falls was schief geht.
     */
    public static void close() throws NamingException {
        if (ServiceLocator.context != null) {
            ServiceLocator.context.close();
            ServiceLocator.context = null;
        }
    }

    /**
     * Initialisiert den {@link InitialContext} mit den {@link Properties}.
     * <p/>
     *
     * @param user String
     * @param password String
     * <p/>
     *
     * @throws NamingException Falls was schief geht
     */
    public static void initContext(final String user, final String password) throws NamingException {
        ServiceLocator.close();

        Properties properties = new Properties();

        properties.put(Context.PROVIDER_URL, "remote://localhost:4447");
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        // properties.put(Context.SECURITY_PRINCIPAL, "AgentK");
        // properties.put(Context.SECURITY_CREDENTIALS, "ak123");

        // properties.put(
        // "jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT",
        // "false");
        // properties.put(
        // "jboss.naming.client.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS",
        // "JBOSS-LOCAL-USER");
        // properties.put("remote.connection.default.callback.handler.class",
        // MyJAASCallbackHandler.class.getName());

        properties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        properties.put("endpoint.name", "client-endpoint");
        properties.put("remote.connections", "default");
        properties.put("remote.connection.default.host", "localhost");
        properties.put("remote.connection.default.port", "4447");
        properties.put("remote.connection.default.username", user);
        properties.put("remote.connection.default.password", password);
        properties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "true");
        properties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
        properties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");

        // TODO Siehe JavaMagazin 5.2013, Seite 86
        EJBClientConfiguration cc = new PropertiesBasedEJBClientConfiguration(properties);
        ContextSelector<EJBClientContext> selector = new ConfigBasedEJBClientContextSelector(cc);
        EJBClientContext.setSelector(selector);

        // properties.put("jboss.naming.client.ejb.context", Boolean.TRUE);

        ServiceLocator.context = new InitialContext(properties);
    }

    /**
     * Führt ein Lookup auf den {@link Context} mit dem Namen durch.
     * <p/>
     *
     * @param name String
     * <p/>
     *
     * @return Object
     * <p/>
     *
     * @throws NamingException Falls was schief geht
     */
    @SuppressWarnings("unchecked")
    public static <T> T lookup(final String name) throws NamingException {
        return (T) ServiceLocator.context.lookup(name);
    }
}
