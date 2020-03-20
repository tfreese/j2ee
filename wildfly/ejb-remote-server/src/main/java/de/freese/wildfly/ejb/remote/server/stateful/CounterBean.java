package de.freese.wildfly.ejb.remote.server.stateful;

import java.util.concurrent.atomic.AtomicInteger;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateful;

/**
 * @author Thomas Freese
 */
@Stateful
@Remote(RemoteCounter.class)
public class CounterBean implements RemoteCounter
{
    /**
     *
     */
    private AtomicInteger count = new AtomicInteger(0);

    /**
     * Erstellt ein neues {@link CounterBean} Object.
     */
    public CounterBean()
    {
        super();
    }

    /**
     * @see de.freese.wildfly.ejb.remote.server.stateful.RemoteCounter#decrement()
     */
    @Override
    public void decrement()
    {
        this.count.decrementAndGet();
    }

    /**
     * @see de.freese.wildfly.ejb.remote.server.stateful.RemoteCounter#getCount()
     */
    @Override
    public int getCount()
    {
        return this.count.get();
    }

    /**
     * @see de.freese.wildfly.ejb.remote.server.stateful.RemoteCounter#increment()
     */
    @Override
    public void increment()
    {
        this.count.incrementAndGet();
    }
}
