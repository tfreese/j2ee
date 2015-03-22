/**
 * Created on 18.03.2015 21:10:40
 */
package de.freese.ejbspring.jms;

import de.freese.ejbspring.facade.IMoneyTransferFacade;
import de.freese.ejbspring.facade.IMoneyTransferRequest;
import de.freese.ejbspring.facade.IMoneyTransferResponse;
import de.freese.ejbspring.facade.impl.MoneyTransferResponseImpl;
import java.util.HashMap;
import java.util.Map;

/**
 * Default-Servicefacade zum Geldtransfer.
 *
 * @author Thomas Freese
 */
public class DefaultMoneyTransferFacade implements IMoneyTransferFacade
{
    /**
     *
     */
    private final Map<String, Double> kontos;

    /**
     * Erstellt ein neues Object.
     */
    public DefaultMoneyTransferFacade()
    {
        super();

        this.kontos = new HashMap<>();
        this.kontos.put("test", 1000.00D);
    }

    /**
     * @see de.freese.ejbspring.facade.IMoneyTransferFacade#transfer(de.freese.ejbspring.facade.IMoneyTransferRequest)
     */
    @Override
    public IMoneyTransferResponse transfer(IMoneyTransferRequest request)
    {
        Double kontostand = this.kontos.get(request.getKonto());

        if (kontostand == null)
        {
            kontostand = request.getBetrag();
        }
        else
        {
            kontostand = kontostand + request.getBetrag();
        }

        this.kontos.put(request.getKonto(), kontostand);

        return new MoneyTransferResponseImpl(kontostand);
    }
}
