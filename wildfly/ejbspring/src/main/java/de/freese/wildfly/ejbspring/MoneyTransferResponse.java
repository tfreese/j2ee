package de.freese.wildfly.ejbspring;

/**
 * @author Thomas Freese
 */
public class MoneyTransferResponse implements IMoneyTransferResponse
{
    /**
     *
     */
    private static final long serialVersionUID = -2949403098563155201L;

    /**
     *
     */
    private final double kontostand;

    /**
     * @param kontostand double
     */
    public MoneyTransferResponse(final double kontostand)
    {
        super();

        this.kontostand = kontostand;
    }

    /**
     * @see de.freese.wildfly.ejbspring.IMoneyTransferResponse#getKontostand()
     */
    @Override
    public double getKontostand()
    {
        return this.kontostand;
    }
}
