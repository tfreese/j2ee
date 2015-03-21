package de.freese.ejbspring.service.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import de.freese.ejbspring.service.IMoneyTransferService;

/**
 * EJB-Implementierung eines {@link IMoneyTransferService}.
 */
// @Stateless(name = "EJBMoneyTransferServiceSLSB", mappedName = "ejb/EJBMoneyTransferServiceSLSB")
@Stateless
@Local(IMoneyTransferService.class)
public class EJBMoneyTransferServiceSLSB extends DefaultMoneyTransferService
{
	/**
     *
     */
	public EJBMoneyTransferServiceSLSB()
	{
		super();
	}

	/**
	 * @see de.freese.ejbspring.service.impl.DefaultMoneyTransferService#transfer(java.lang.String, double)
	 */
	@Override
	public double transfer(final String konto, final double betrag)
	{
		return super.transfer(konto, betrag);
	}
}
