/**
 * Created on 18.03.2015 20:26:32
 */
package de.freeese.ejbspring.service;

/**
 * Interface für einen Service.
 *
 * @author Thomas Freese
 */
public interface IMoneyTransferService
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
