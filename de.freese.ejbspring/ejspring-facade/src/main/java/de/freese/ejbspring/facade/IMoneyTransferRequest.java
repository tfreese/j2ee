package de.freese.ejbspring.facade;

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
     * @return amount Double
     */
    public Double getBetrag();
}
