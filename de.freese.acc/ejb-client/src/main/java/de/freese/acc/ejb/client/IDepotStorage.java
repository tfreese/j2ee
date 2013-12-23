package de.freese.acc.ejb.client;

import de.freese.acc.model.Depot;

/**
 * @author Thomas Freese
 */
public interface IDepotStorage
{
	/**
	 * @param mailAddress String
	 * @return boolean
	 */
	public boolean mailAddressUsed(String mailAddress);

	/**
	 * @param mailAddress String
	 * @param password String
	 * @return {@link Depot}
	 */
	public Depot register(String mailAddress, String password);
}
