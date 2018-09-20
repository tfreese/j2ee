package de.freese.wildfly.ejbspring;

import java.io.Serializable;

/**
 * Response vom Geldtransfer.
 */
public interface IMoneyTransferResponse extends Serializable
{
    /**
     * Liefert das verbleibende Guthaben.
     *
     * @return double
     */
    public double getKontostand();
}
