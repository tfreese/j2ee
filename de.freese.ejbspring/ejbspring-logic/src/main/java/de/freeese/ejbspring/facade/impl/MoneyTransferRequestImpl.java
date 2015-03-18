package de.freeese.ejbspring.facade.impl;

import de.freeese.ejbspring.facade.IMoneyTransferRequest;

public class MoneyTransferRequestImpl implements IMoneyTransferRequest
{
    /**
     *
     */
    private static final long serialVersionUID = 1374492424533152653L;

    /**
     *
     */
    private final String konto;

    /**
     *
     */
    private final Double betrag;

    /**
     * @param konto  String
     * @param betrag Double
     */
    public MoneyTransferRequestImpl(String konto, Double betrag)
    {
        super();

        this.konto = konto;
        this.betrag = betrag;
    }

    @Override
    public String getKonto()
    {
        return this.konto;
    }

    @Override
    public Double getBetrag()
    {
        return this.betrag;
    }
}
