package de.freese.ejbspring.facade;

/**
 * Servicefacade zum Geldtransfer.
 */
public interface IMoneyTransferFacade
{
    /**
     * Request zum Geldtransfer.
     *
     * @param request {@link IMoneyTransferRequest}
     *
     * @return {@link IMoneyTransferResponse}
     */
    public IMoneyTransferResponse transfer(IMoneyTransferRequest request);
}
