package de.freese.wildfly.ejbspring.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Basis-Implementierung eines {@link IMoneyTransferService}.
 */
public class DefaultMoneyTransferService implements IMoneyTransferService
{
    /**
     *
     */
    private final Map<String, Double> kontos;

    /**
     *
     */
    public DefaultMoneyTransferService()
    {
        super();

        this.kontos = new HashMap<>();
        this.kontos.put("test", 1000.00D);
    }

    /**
     * @see de.freese.wildfly.ejbspring.service.IMoneyTransferService#transfer(java.lang.String, double)
     */
    @Override
    public double transfer(final String konto, final double betrag)
    {
        Double kontostand = this.kontos.get(konto);

        if (kontostand == null)
        {
            kontostand = betrag;
        }
        else
        {
            kontostand = kontostand + betrag;
        }

        this.kontos.put(konto, kontostand);

        return kontostand;
    }
}