/**
 * Created on 18.03.2015 20:32:49
 */
package de.freeese.ejbspring.facade.impl;

import de.freeese.ejbspring.facade.IMoneyTransferFacade;
import de.freeese.ejbspring.facade.IMoneyTransferRequest;
import de.freeese.ejbspring.facade.IMoneyTransferResponse;
import de.freeese.ejbspring.service.IMoneyTransferService;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

/**
 * EJB-Servicefacade zum Geldtransfer.
 *
 * @author Thomas Freese
 */
@Stateless(name = "EJBMoneyTransferFacadeSLSB", mappedName = "ejb/EJBMoneyTransferFacadeSLSB")
@Local(IMoneyTransferFacade.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class EJBMoneyTransferFacadeSLSB implements IMoneyTransferFacade
{
    @Resource
    private IMoneyTransferService moneyService;

    /**
     * Erstellt ein neues Object.
     */
    public EJBMoneyTransferFacadeSLSB()
    {
        super();
    }

    @Override
    public IMoneyTransferResponse transfer(IMoneyTransferRequest request)
    {
        double kontostand = this.moneyService.transfer(request.getKonto(), request.getBetrag());

        return new MoneyTransferResponseImpl(kontostand);
    }
}
