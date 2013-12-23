package de.freese.acc.ejb;

import de.freese.acc.ejb.client.ITrader;
import de.freese.acc.model.Depot;
import de.freese.acc.model.Security;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Thomas Freese
 */
@Stateless
@Remote(ITrader.class)
public class TraderBean implements ITrader
{
	/**
	 * 
	 */
	@PersistenceContext
	private EntityManager entityManager = null;

	/**
	 * Erstellt ein neues {@link TraderBean} Object.
	 */
	public TraderBean()
	{
		super();
	}

	/**
	 * @see de.freese.acc.ejb.client.ITrader#buy(de.freese.acc.model.Depot, de.freese.acc.model.Security, int)
	 */
	@Override
	public Depot buy(final Depot depot, final Security securityToBuy, final int count)
	{
		depot.addBuyOrder(securityToBuy, count);
		Depot mergedDeport = this.entityManager.merge(depot);
		this.entityManager.persist(mergedDeport);

		return mergedDeport;
	}
}
