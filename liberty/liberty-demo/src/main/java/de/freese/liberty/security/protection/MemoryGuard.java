package de.freese.liberty.security.protection;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.function.DoubleSupplier;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author Thomas Freese
 */
@ApplicationScoped
public class MemoryGuard {
    // Refresh HeapUsage every n Seconds.
    private static final Duration TIME_TO_LIVE = Duration.ofSeconds(5L);

    private Clock clock;
    private double heapUsage;
    private DoubleSupplier memoryThresholdSupplier = () -> 95D;
    private Instant refreshedAt;

    public synchronized boolean isOverloaded() {
        final Instant now = clock.instant();

        if (now.isAfter(refreshedAt.plus(TIME_TO_LIVE))) {
            refresh();

            refreshedAt = now;
        }

        return heapUsage >= memoryThresholdSupplier.getAsDouble();
    }

    public void setMemoryThresholdSupplier(final DoubleSupplier memoryThresholdSupplier) {
        this.memoryThresholdSupplier = Objects.requireNonNull(memoryThresholdSupplier, "memoryThresholdSupplier required");
    }

    @PostConstruct
    void init() {
        clock = Clock.systemUTC();

        refresh();

        refreshedAt = clock.instant();
    }

    private void refresh() {
        final MemoryUsage memoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        final long usedMemory = memoryUsage.getUsed();
        final long maxMemory = memoryUsage.getMax() > 0 ? memoryUsage.getMax() : memoryUsage.getCommitted();

        heapUsage = ((double) usedMemory / maxMemory) * 100D;
    }
}