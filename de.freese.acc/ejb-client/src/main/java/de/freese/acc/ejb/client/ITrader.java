package de.freese.acc.ejb.client;

import de.freese.acc.model.Depot;
import de.freese.acc.model.Security;

/**
 * @author Thomas Freese
 */
public interface ITrader
{
	/**
	 * @param depot {@link Depot}
	 * @param securityToBuy {@link Security}
	 * @param count int
	 * @return {@link Depot}
	 */
	public Depot buy(Depot depot, Security securityToBuy, int count);
}
