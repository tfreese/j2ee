package de.freeese.ejbspring.facade.impl;

import de.freeese.ejbspring.facade.IMoneyTransferResponse;

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
     *
     * @param kontostand Double
     */
    public MoneyTransferResponseImpl(Double kontostand)
    {
        super();

        this.kontostand = kontostand;
    }

    @Override
    public Double getKontostand()
    {
        return this.kontostand;
    }
}
