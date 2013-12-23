package de.freese.acc.ejb;

import de.freese.acc.ejb.client.ISecurityStorage;
import de.freese.acc.model.Security;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Thomas Freese
 */
@Stateless
@Remote(ISecurityStorage.class)
public class SecurityStorageBean implements ISecurityStorage
{
	/**
	 * 
	 */
	@PersistenceContext
	private EntityManager entityManager = null;

	/**
	 * Erstellt ein neues {@link SecurityStorageBean} Object.
	 */
	public SecurityStorageBean()
	{
		super();
	}

	/**
	 * @see de.freese.acc.ejb.client.ISecurityStorage#getAllSecurities()
	 */
	@Override
	public List<Security> getAllSecurities()
	{
		return this.entityManager.createNamedQuery("findAllSecurities", Security.class).getResultList();
	}
}
