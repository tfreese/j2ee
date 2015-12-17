package de.freese.wildfly.ejbspring.facade;

import de.freese.wildfly.ejbspring.IMoneyTransferRequest;
import de.freese.wildfly.ejbspring.IMoneyTransferResponse;
import de.freese.wildfly.ejbspring.service.IMoneyTransferService;

/**
 * Servicefacade zum Geldtransfer.
 */
public interface IMoneyTransferFacade
{
    /**
     * @return {@link IMoneyTransferService}
     */
    public IMoneyTransferService getTransferService();

    /**
     * Request zum Geldtransfer.
     *
     * @param request {@link IMoneyTransferRequest}
     * @return {@link IMoneyTransferResponse}
     */
    public IMoneyTransferResponse transfer(IMoneyTransferRequest request);
}
