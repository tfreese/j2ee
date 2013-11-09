/**
 * Created: 14.12.2012
 */

package de.freese.agentportal.server.dao;

import de.freese.agentportal.common.model.SecretNews;
import de.freese.agentportal.server.cdi.Resources;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

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
	private EntityManager entityManager = null;

	/**
	 * 
	 */
	@Inject
	private UserTransaction utx = null;

	/**
	 * Erstellt ein neues {@link AbstractSecretNewsDAO} Object.
	 */
	public AbstractSecretNewsDAO()
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

		SecretNews entity = (SecretNews) query.getSingleResult();

		return entity;
	}

	/**
	 * @see de.freese.agentportal.server.dao.ISecretNewsDAO#getNews()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SecretNews> getNews()
	{
		Query query = this.entityManager.createNamedQuery("selectAllSecretNews");
		query.setParameter("securitylevel", getSecurityLevel()).getResultList();

		List<SecretNews> news = query.getResultList();

		return news;
	}

	/**
	 * @return int
	 */
	protected abstract int getSecurityLevel();
}
