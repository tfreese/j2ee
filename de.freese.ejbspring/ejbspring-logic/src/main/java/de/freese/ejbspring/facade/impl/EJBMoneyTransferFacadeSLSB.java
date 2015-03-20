/**
 * Created on 18.03.2015 20:32:49
 */
package de.freese.ejbspring.facade.impl;

import de.freese.ejbspring.facade.IMoneyTransferFacade;
import de.freese.ejbspring.facade.IMoneyTransferFacadeRemote;
import de.freese.ejbspring.facade.IMoneyTransferRequest;
import de.freese.ejbspring.facade.IMoneyTransferResponse;
import de.freese.ejbspring.service.IMoneyTransferService;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

/**
 * EJB-Servicefacade zum Geldtransfer mit Spring-Service.
 *
 * @author Thomas Freese
 */
@Stateless(name = "EJBMoneyTransferFacadeSLSB", mappedName = "ejb/EJBMoneyTransferFacadeSLSB")
@Local(IMoneyTransferFacade.class)
@Remote(IMoneyTransferFacadeRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class EJBMoneyTransferFacadeSLSB implements IMoneyTransferFacade, IMoneyTransferFacadeRemote
{
    /**
     *
     */
    @Autowired
    @Qualifier("springMoneyTransferService")
    //@Inject
    //@Named("springMoneyTransferService")
    private IMoneyTransferService moneyService;

    /**
     * Erstellt ein neues Object.
     */
    public EJBMoneyTransferFacadeSLSB()
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
     * @see de.freese.ejbspring.facade.IMoneyTransferFacadeRemote#transfer(java.lang.String, double)
     */
    @Override
    public double transfer(String konto, double betrag)
    {
        return this.moneyService.transfer(konto, betrag);
    }
}
