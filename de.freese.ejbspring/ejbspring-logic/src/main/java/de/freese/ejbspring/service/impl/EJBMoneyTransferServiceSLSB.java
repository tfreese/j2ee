package de.freese.ejbspring.service.impl;

import de.freese.ejbspring.service.IMoneyTransferService;
import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 * EJB-Implementierung eines {@link IMoneyTransferService}.
 */
@Stateless(name = "EJBMoneyTransferServiceSLSB", mappedName = "ejb/EJBMoneyTransferServiceSLSB")
@Local(IMoneyTransferService.class)
public class EJBMoneyTransferServiceSLSB extends DefaultMoneyTransferService
{
    /**
     *
     */
    public EJBMoneyTransferServiceSLSB()
    {
        super();
    }

//    @Override
//    @Resource(name = "initialBalance")
//    public void setBalance(Double balance)
//    {
//        super.setBalance(balance);
//    }
}
