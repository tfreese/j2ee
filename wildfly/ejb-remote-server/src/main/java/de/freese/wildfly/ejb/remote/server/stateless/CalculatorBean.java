package de.freese.wildfly.ejb.remote.server.stateless;

import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * @author Thomas Freese
 */
@Stateless
@Remote(RemoteCalculator.class)
public class CalculatorBean implements RemoteCalculator
{
    /**
     * Erstellt ein neues {@link CalculatorBean} Object.
     */
    public CalculatorBean()
    {
        super();
    }

    /**
     * @see de.freese.wildfly.ejb.remote.server.stateless.RemoteCalculator#add(int, int)
     */
    @Override
    public int add(final int a, final int b)
    {
        return a + b;
    }

    /**
     * @see de.freese.wildfly.ejb.remote.server.stateless.RemoteCalculator#subtract(int, int)
     */
    @Override
    public int subtract(final int a, final int b)
    {
        return a - b;
    }
}
