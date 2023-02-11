/**
 * Created: 19.05.2012
 */

package de.freese.agentportal.client.ejb;

import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.common.service.ISecretNewsService;

/**
 * @author Thomas Freese
 */
public class EJBUnsecureClient extends AbstractEJBClient {
    /**
     * @throws Exception Falls was schief geht.
     */
    @BeforeClass
    public static void setupClass() throws Exception {
        ServiceLocator.initContext("AustinPowers", "ap123");
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @AfterClass
    public static void shutdownClass() throws Exception {
        ServiceLocator.close();
    }

    /**
     * Erstellt ein neues {@link EJBUnsecureClient} Object.
     */
    public EJBUnsecureClient() {
        super();
    }

    /**
     * @see de.freese.agentportal.client.ejb.AbstractEJBClient#assertHigh(de.freese.agentportal.common.service.ISecretNewsService)
     */
    @Override
    protected List<SecretNews> assertHigh(final ISecretNewsService service) {
        try {
            service.getAllSecretNews4High();
            Assert.assertTrue(false);
        }
        catch (Exception ex) {
            Assert.assertTrue(true);
        }

        return Collections.emptyList();
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
