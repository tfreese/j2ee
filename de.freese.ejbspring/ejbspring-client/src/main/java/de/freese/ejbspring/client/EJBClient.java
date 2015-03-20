// Erzeugt: 20.03.2015
package de.freese.ejbspring.client;

import de.freese.ejbspring.facade.IMoneyTransferFacadeRemote;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 *
 * @author Thomas Freese (AuVi)
 */
public class EJBClient
{
    /**
     * @param args String[]
     * <p>
     * @throws Exception Falls was schief geht.
     */
    public static void main(final String[] args) throws Exception
    {
//        Properties clientProperties = new Properties();
//        clientProperties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
//        clientProperties.put("remote.connections", "default");
//        clientProperties.put("remote.connection.default.port", 4447);
//        clientProperties.put("remote.connection.default.host", "localhost");
//        clientProperties.put("remote.connection.default.username", "Admin");
//        clientProperties.put("remote.connection.default.password", "Admin");
//        clientProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS",
//                             "false");
//
//        EJBClientConfiguration ejbClientConfiguration = new PropertiesBasedEJBClientConfiguration(clientProperties);
//        ContextSelector<EJBClientContext> contextSelector = new ConfigBasedEJBClientContextSelector(
//                ejbClientConfiguration);
//        EJBClientContext.setSelector(contextSelector);

        Properties props = new Properties();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        props.put("jboss.naming.client.ejb.context", true);

        //String jndi = "java:jboss/exported/ejbspring-ear-0.0.1-SNAPSHOT/ejbspring-logic-0.0.1-SNAPSHOT/EJBMoneyTransferFacadeSLSB!de.freese.ejbspring.facade.IMoneyTransferFacadeRemote";
        String jndi = "ejb:ejbspring-ear-0.0.1-SNAPSHOT/ejbspring-logic-0.0.1-SNAPSHOT/EJBMoneyTransferFacadeSLSB!de.freese.ejbspring.facade.IMoneyTransferFacadeRemote";

        Context ctx = new InitialContext(props);
        IMoneyTransferFacadeRemote facade = (IMoneyTransferFacadeRemote) ctx.lookup(jndi);
        System.out.println(facade.transfer("test", 1000.0D));
        System.out.println(facade.transfer("test", 1000.0D));
    }
}
