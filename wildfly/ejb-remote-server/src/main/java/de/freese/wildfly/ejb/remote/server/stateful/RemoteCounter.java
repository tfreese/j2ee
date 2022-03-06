package de.freese.wildfly.ejb.remote.server.stateful;

/**
 * @author Thomas Freese
 */
public interface RemoteCounter
{
    /**
     *
     */
    void decrement();

    /**
     * @return int
     */
    int getCount();

    /**
     *
     */
    void increment();
}
