package de.freeese.ejbspring.facade;

import java.io.Serializable;

/**
 * Response vom Geldtransfer.
 */
public interface IMoneyTransferResponse extends Serializable
{
    /**
     * Liefert das verbleibende Guthaben.
     *
     * @return Double
     */
    public Double getKontostand();
}
