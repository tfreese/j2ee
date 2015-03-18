package de.freeese.ejbspring.service.impl;

import de.freeese.ejbspring.service.IMoneyTransferService;
import javax.ejb.Local;
import javax.ejb.Singleton;

/**
 * Singleton EJB implementation of <code>MoneyTransferService</code>.
 */
@Local(IMoneyTransferService.class)
@Singleton(name = "EJBSingletonMoneyTransferService", mappedName = "ejb/EJBSingletonMoneyTransferService")
public class EJBSingletonMoneyTransferService extends DefaultMoneyTransferService
{
    /**
     *
     */
    public EJBSingletonMoneyTransferService()
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
