package de.freese.wildfly.ejbspring;

import java.io.Serializable;

/**
 * Request zum Geldtransfer.
 */
public interface IMoneyTransferRequest extends Serializable
{
    /**
     * Liefert das Konto.
     *
     * @return amount Double
     */
    public String getKonto();

    /**
     * Liefert den Betrag.
     *
     * @return amount double
     */
    public double getBetrag();
}
