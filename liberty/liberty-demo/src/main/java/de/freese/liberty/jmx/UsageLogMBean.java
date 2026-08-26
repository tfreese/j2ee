package de.freese.liberty.jmx;

import java.util.Set;

/**
 * @author Thomas Freese
 * @since 21.05.2013
 */
@FunctionalInterface
public interface UsageLogMBean {
    Set<String> getParameters();
}
