// Created: 14.12.2012
package de.freese.agentportal.server.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.server.cdi.Resources;

/**
 * @author Thomas Freese
 */
public abstract class AbstractSecretNewsDAO implements ISecretNewsDAO
{
    /**
     *
     */
    // @Inject
    // @AgentPortalEM
    @PersistenceContext(unitName = Resources.EM_UNIT)
    private EntityManager entityManager;

    // /**
    // *
    // */
    // @Inject
    // private UserTransaction utx = null;

    /**
     * Erstellt ein neues {@link AbstractSecretNewsDAO} Object.
     */
    protected AbstractSecretNewsDAO()
    {
        super();
    }

    /**
     * @see de.freese.agentportal.server.dao.ISecretNewsDAO#get(long)
     */
    @Override
    public SecretNews get(final long id)
    {
        Query query = this.entityManager.createQuery("select n from SecretNewsEntity n where id = :id");
        query.setParameter("id", id);

        return (SecretNews) query.getSingleResult();
    }

    /**
     * @see de.freese.agentportal.server.dao.ISecretNewsDAO#getNews()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<SecretNews> getNews()
    {
        Query query = this.entityManager.createNamedQuery("selectAllSecretNews");
        query.setParameter("securityLevel", getSecurityLevel()).getResultList();

        return query.getResultList();
    }

    /**
     * @return int
     */
    protected abstract int getSecurityLevel();
}
