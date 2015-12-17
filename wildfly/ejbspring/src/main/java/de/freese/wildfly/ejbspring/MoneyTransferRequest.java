package de.freese.wildfly.ejbspring;

/**
 * @author Thomas Freese
 */
public class MoneyTransferRequest implements IMoneyTransferRequest
{
    /**
     *
     */
    private static final long serialVersionUID = 1374492424533152653L;

    /**
     *
     */
    private final double betrag;

    /**
     *
     */
    private final String konto;

    /**
     * @param konto  String
     * @param betrag Double
     */
    public MoneyTransferRequest(final String konto, final double betrag)
    {
        super();

        this.konto = konto;
        this.betrag = betrag;
    }

    /**
     * @see de.freese.wildfly.ejbspring.IMoneyTransferRequest#getBetrag()
     */
    @Override
    public double getBetrag()
    {
        return this.betrag;
    }

    /**
     * @see de.freese.wildfly.ejbspring.IMoneyTransferRequest#getKonto()
     */
    @Override
    public String getKonto()
    {
        return this.konto;
    }
}
