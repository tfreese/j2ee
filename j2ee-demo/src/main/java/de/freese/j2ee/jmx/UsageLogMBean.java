// Created: 21.05.2013
package de.freese.j2ee.jmx;

import java.util.Set;

/**
 * @author Thomas Freese
 */
@FunctionalInterface
public interface UsageLogMBean
{
    Set<String> getParameters();
}
