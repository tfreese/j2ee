/**
 * Created on 18.03.2015 21:10:40
 */
package de.freese.ejbspring.facade.impl;

import de.freese.ejbspring.facade.IMoneyTransferFacade;
import de.freese.ejbspring.facade.IMoneyTransferRequest;
import de.freese.ejbspring.facade.IMoneyTransferResponse;
import de.freese.ejbspring.service.IMoneyTransferService;

/**
 * Spring-Servicefacade zum Geldtransfer mit EJB-Service.
 *
 * @author Thomas Freese
 */
public class SpringMoneyTransferFacade implements IMoneyTransferFacade
{
    /**
     *
     */
    private IMoneyTransferService moneyService;

    /**
     * Erstellt ein neues Object.
     */
    public SpringMoneyTransferFacade()
    {
        super();
    }

    /**
     * @see de.freese.ejbspring.facade.IMoneyTransferFacade#transfer(de.freese.ejbspring.facade.IMoneyTransferRequest)
     */
    @Override
    public IMoneyTransferResponse transfer(IMoneyTransferRequest request)
    {
        double kontostand = this.moneyService.transfer(request.getKonto(), request.getBetrag());

        return new MoneyTransferResponseImpl(kontostand);
    }

    /**
     * @param moneyService {@link IMoneyTransferService}
     */
    public void setMoneyService(IMoneyTransferService moneyService)
    {
        this.moneyService = moneyService;
    }
}
