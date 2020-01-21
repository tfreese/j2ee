package de.freese.wildfly.ejb.remote.server.stateless;

/**
 * @author Thomas Freese
 */
public interface RemoteCalculator
{
    /**
     * @param a int
     * @param b int
     * @return int
     */
    public int add(int a, int b);

    /**
     * @param a int
     * @param b int
     * @return int
     */
    public int subtract(int a, int b);
}
