/**
 * Created: 19.05.2012
 */

package de.freese.agentportal.client.ejb;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.common.service.ISecretNewsService;

/**
 * @author Thomas Freese
 */
public abstract class AbstractEJBClient {
    /**
     *
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Erstellt ein neues {@link AbstractEJBClient} Object.
     */
    public AbstractEJBClient() {
        super();
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @Test
    public void testHigh() throws Exception {
        getLogger().info("testHigh");

        ISecretNewsService service = getService();

        List<SecretNews> news = assertHigh(service);

        for (SecretNews newsEntity : news) {
            getLogger().info(newsEntity.toString());
        }
    }

    /**
     * @throws Exception Falls was schief geht.
     */
    @Test
    public void testLow() throws Exception {
        getLogger().info("testLow");

        ISecretNewsService service = getService();

        List<SecretNews> news = assertLow(service);

        for (SecretNews newsEntity : news) {
            getLogger().info(newsEntity.toString());
        }
    }

    /**
     * @param service {@link ISecretNewsService}
     *
     * @return {@link List}
     */
    protected abstract List<SecretNews> assertHigh(ISecretNewsService service);

    /**
     * @param service {@link ISecretNewsService}
     *
     * @return {@link List}
     */
    protected abstract List<SecretNews> assertLow(ISecretNewsService service);

    /**
     * @return {@link Logger}
     */
    protected Logger getLogger() {
        return this.logger;
    }

    /**
     * @return {@link ISecretNewsService}
     *
     * @throws Exception Falls was schief geht.
     */
    protected ISecretNewsService getService() throws Exception {
        ISecretNewsService service = ServiceLocator.lookup("de.freese.agentportal.server/SecretNewsSessionBean!de.freese.agentportal.common.service.ISecretNewsService");

        return service;
    }
}
