package de.freese.ejbspring.facade;

/**
 * Servicefacade zum Geldtransfer.
 */
public interface IMoneyTransferFacadeRemote
{
    /**
     * Verrechnet den Betrag mit dem Guthaben des Kontos.
     *
     * @param konto  String
     * @param betrag double
     *
     * @return double
     */
    public double transfer(String konto, double betrag);
}
