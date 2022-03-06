package de.freese.wildfly.ejb.remote.server.stateless;

/**
 * @author Thomas Freese
 */
public interface RemoteCalculator
{
    /**
     * @param a int
     * @param b int
     *
     * @return int
     */
    int add(int a, int b);

    /**
     * @param a int
     * @param b int
     *
     * @return int
     */
    int subtract(int a, int b);
}
