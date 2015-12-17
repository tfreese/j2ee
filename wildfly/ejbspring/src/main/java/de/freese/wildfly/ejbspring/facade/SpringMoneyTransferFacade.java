/**
 * Created on 18.03.2015 21:10:40
 */
package de.freese.wildfly.ejbspring.facade;

import de.freese.wildfly.ejbspring.IMoneyTransferRequest;
import de.freese.wildfly.ejbspring.IMoneyTransferResponse;
import de.freese.wildfly.ejbspring.MoneyTransferResponse;
import de.freese.wildfly.ejbspring.service.IMoneyTransferService;

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
    private IMoneyTransferService ejbMoneyService = null;

    /**
     * Erstellt ein neues Object.
     */
    public SpringMoneyTransferFacade()
    {
        super();
    }

    /**
     * @see de.freese.wildfly.ejbspring.facade.IMoneyTransferFacade#getTransferService()
     */
    @Override
    public IMoneyTransferService getTransferService()
    {
        return this.ejbMoneyService;
    }

    /**
     * @param ejbMoneyService {@link IMoneyTransferService}
     */
    public void setEjbMoneyService(final IMoneyTransferService ejbMoneyService)
    {
        this.ejbMoneyService = ejbMoneyService;
    }

    /**
     * @see de.freese.wildfly.ejbspring.facade.IMoneyTransferFacade#transfer(de.freese.wildfly.ejbspring.IMoneyTransferRequest)
     */
    @Override
    public IMoneyTransferResponse transfer(final IMoneyTransferRequest request)
    {
        double kontostand = this.ejbMoneyService.transfer(request.getKonto(), request.getBetrag());

        return new MoneyTransferResponse(kontostand);
    }
}
