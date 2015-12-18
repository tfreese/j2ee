package de.freese.wildfly.ejb.remote.server.stateful;

/**
 * @author Thomas Freese
 */
public interface RemoteCounter
{
    /**
     *
     */
    public void decrement();

    /**
     * @return int
     */
    public int getCount();

    /**
     *
     */
    public void increment();
}
