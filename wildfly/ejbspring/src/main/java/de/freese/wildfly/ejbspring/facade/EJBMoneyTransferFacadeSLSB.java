/**
 * Created on 18.03.2015 20:32:49
 */
package de.freese.wildfly.ejbspring.facade;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import de.freese.wildfly.ejbspring.IMoneyTransferRequest;
import de.freese.wildfly.ejbspring.IMoneyTransferResponse;
import de.freese.wildfly.ejbspring.MoneyTransferResponse;
import de.freese.wildfly.ejbspring.service.IMoneyTransferService;
import de.freese.wildfly.ejbspring.service.SpringMoneyTransferService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * EJB-Servicefacade zum Geldtransfer mit Spring-Service.
 *
 * @author Thomas Freese
 */
// @Stateless(name = "EJBMoneyTransferFacadeSLSB", mappedName = "ejb/EJBMoneyTransferFacadeSLSB")
@Stateless()
@Local(IMoneyTransferFacade.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class EJBMoneyTransferFacadeSLSB implements IMoneyTransferFacade
{
    /**
     *
     */
    //@Resource(name = "springMoneyTransferService")
    @Autowired
    private SpringMoneyTransferService springMoneyService;

    /**
     * Erstellt ein neues Object.
     */
    public EJBMoneyTransferFacadeSLSB()
    {
        super();
    }

    /**
     * @see de.freese.wildfly.ejbspring.facade.IMoneyTransferFacade#getTransferService()
     */
    @Override
    public IMoneyTransferService getTransferService()
    {
        return this.springMoneyService;
    }

    /**
     * @see de.freese.wildfly.ejbspring.facade.IMoneyTransferFacade#transfer(de.freese.wildfly.ejbspring.IMoneyTransferRequest)
     */
    @Override
    public IMoneyTransferResponse transfer(final IMoneyTransferRequest request)
    {
        double kontostand = this.springMoneyService.transfer(request.getKonto(), request.getBetrag());

        return new MoneyTransferResponse(kontostand);
    }
}
