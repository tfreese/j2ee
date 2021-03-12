package de.freese.j2ee.cdi;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;

/**
 * @author Thomas Freese
 */
@Named
@RequestScoped
public class FakultaetView
{
    /**
     *
     */
    private long fakt;

    /**
     *
     */
    @Inject
    @Recursive
    private IFakultaet fakultaet;

    /**
     *
     */
    @Max(5)
    private int n;

    /**
     * @return String
     */
    public String calculate()
    {
        System.out.println("Test " + getN());
        setFakt(this.fakultaet.getFakultaet(getN()));

        return null;
    }

    /**
     * @return return
     */
    public long getFakt()
    {
        return this.fakt;
    }

    /**
     * @return int
     */
    public int getN()
    {
        return this.n;
    }

    /**
     * @param fakt long
     */
    public void setFakt(final long fakt)
    {
        this.fakt = fakt;
    }

    /**
     * @param n int
     */
    public void setN(final int n)
    {
        this.n = n;
    }
}
