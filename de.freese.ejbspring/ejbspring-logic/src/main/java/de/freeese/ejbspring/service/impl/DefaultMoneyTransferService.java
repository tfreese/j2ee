package de.freeese.ejbspring.service.impl;

import de.freeese.ejbspring.service.IMoneyTransferService;

/**
 * Basis-Implementierung eines IMoneyTransferService.
 */
public class DefaultMoneyTransferService implements IMoneyTransferService
{
    /**
     *
     */
    public DefaultMoneyTransferService()
    {
        super();
    }

    /**
     *
     * @param konto  String
     * @param betrag double
     *
     * @return double
     */
    @Override
    public double transfer(String konto, double betrag)
    {
        // TODO
        return betrag;
    }
}
