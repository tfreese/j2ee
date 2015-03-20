package de.freese.ejbspring.service.impl;

import de.freese.ejbspring.service.IMoneyTransferService;
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
     * @see de.freese.ejbspring.service.IMoneyTransferService#transfer(java.lang.String, double)
     */
    @Override
    public double transfer(String konto, double betrag)
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
