package de.freese.acc.ejb;

import de.freese.acc.model.Rate;
import de.freese.acc.model.Security;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Thomas Freese
 */
@Startup
@Singleton
public class SecuritiesLoaderBean
{
	/**
	 * 
	 */
	@PersistenceContext
	private EntityManager entityManager = null;

	/**
	 * Erstellt ein neues {@link SecuritiesLoaderBean} Object.
	 */
	public SecuritiesLoaderBean()
	{
		super();
	}

	/**
	 * 
	 */
	@PostConstruct
	public void loadSecurities()
	{
		Long securitiesCount = this.entityManager.createNamedQuery("countSecurities", Long.class).getSingleResult();

		if (securitiesCount > 0)
		{
			System.out.println("SecuritiesLoaderBean: Securities already exist.");
			return;
		}

		System.out.println("SecuritiesLoaderBean.loadSecurities()");
		Security security = new Security();
		security.setIsin("DE0123");
		security.getRates().add(new Rate(new Date(), 103.57f));
		this.entityManager.persist(security);

		System.out.println("Id: " + security.getId());
	}
}
