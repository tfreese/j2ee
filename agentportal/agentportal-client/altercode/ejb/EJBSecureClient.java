/**
 * Created: 19.05.2012
 */

package de.freese.agentportal.client.ejb;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.common.service.ISecretNewsService;

/**
 * @author Thomas Freese
 */
public class EJBSecureClient extends AbstractEJBClient {
    /**
     * @throws Exception Falls was schief geht.
     */
    @BeforeClass
    public static void setupClass() throws Exception {
        // System.setProperty("java.security.auth.login.config",
        // "src/main/resources/META-INF/auth.conf");

        // CallbackHandler callbackHandler = null;
        // callbackHandler = new EJBClientCallbackHandler("AgentK", "ak123".toCharArray());
        // callbackHandler = new UsernamePasswordHandler("AgentK", "ak123".toCharArray());
        // callbackHandler =
        // new PasswordClientCallbackHandler("AgentK", "AgentPortalRealm",
        // "ak123".toCharArray());
        //
        // LoginContext loginContext = new LoginContext("AgentPortalRealm", callbackHandler);
        // loginContext.login();

        // MyJAASCallbackHandler.setCredential("AgentK", "ak123", "AgentPortalRealm");
        ServiceLocator.initContext("AgentK", "ak123");
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @AfterClass
    public static void shutdownClass() throws Exception {
        ServiceLocator.close();
    }

    /**
     * Erstellt ein neues {@link EJBSecureClient} Object.
     */
    public EJBSecureClient() {
        super();
    }

    /**
     * @see de.freese.agentportal.client.ejb.AbstractEJBClient#assertHigh(de.freese.agentportal.common.service.ISecretNewsService)
     */
    @Override
    protected List<SecretNews> assertHigh(final ISecretNewsService service) {
        List<SecretNews> news = service.getAllSecretNews4High();
        Assert.assertTrue(!news.isEmpty());

        return news;
    }

    /**
     * @see de.freese.agentportal.client.ejb.AbstractEJBClient#assertLow(de.freese.agentportal.common.service.ISecretNewsService)
     */
    @Override
    protected List<SecretNews> assertLow(final ISecretNewsService service) {
        List<SecretNews> news = service.getAllSecretNews4Low();
        Assert.assertTrue(!news.isEmpty());

        return news;
    }
}
