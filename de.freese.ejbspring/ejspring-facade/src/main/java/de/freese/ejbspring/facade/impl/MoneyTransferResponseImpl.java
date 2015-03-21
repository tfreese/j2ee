package de.freese.ejbspring.facade.impl;

import de.freese.ejbspring.facade.IMoneyTransferResponse;

/**
 * @author Thomas Freese
 */
public class MoneyTransferResponseImpl implements IMoneyTransferResponse
{
	/**
     *
     */
	private static final long serialVersionUID = -2949403098563155201L;

	/**
     *
     */
	private final Double kontostand;

	/**
	 * @param kontostand Double
	 */
	public MoneyTransferResponseImpl(final Double kontostand)
	{
		super();

		this.kontostand = kontostand;
	}

	/**
	 * @see de.freese.ejbspring.facade.IMoneyTransferResponse#getKontostand()
	 */
	@Override
	public Double getKontostand()
	{
		return this.kontostand;
	}
}
