package de.freeese.ejbspring.facade;

/**
 * Servicefacade zum Geldtransfer.
 */
public interface IMoneyTransferFacade
{
    /**
     * Request zum Geldtransfer.
     *
     * @param request IMoneyTransferRequest
     *
     * @return IMoneyTransferResponse
     */
    public IMoneyTransferResponse transfer(IMoneyTransferRequest request);
}
