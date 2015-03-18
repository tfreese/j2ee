/**
 * Created on 18.03.2015 21:10:40
 */
package de.freeese.ejbspring.facade.impl;

import de.freeese.ejbspring.facade.IMoneyTransferFacade;
import de.freeese.ejbspring.facade.IMoneyTransferRequest;
import de.freeese.ejbspring.facade.IMoneyTransferResponse;
import de.freeese.ejbspring.service.IMoneyTransferService;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Spring-Servicefacade zum Geldtransfer.
 *
 * @author Thomas Freese
 */
@Component
public class SpringMoneyTransferFacade implements IMoneyTransferFacade
{
    @Resource
    private IMoneyTransferService moneyService;

    /**
     * Erstellt ein neues Object.
     */
    public SpringMoneyTransferFacade()
    {
        super();
    }

    @Override
    public IMoneyTransferResponse transfer(IMoneyTransferRequest request)
    {
        double kontostand = this.moneyService.transfer(request.getKonto(), request.getBetrag());

        return new MoneyTransferResponseImpl(kontostand);
    }

    /**
     *
     * @param moneyService IMoneyTransferService
     */
    public void setMoneyService(IMoneyTransferService moneyService)
    {
        this.moneyService = moneyService;
    }
}
