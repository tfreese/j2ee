/**
 * Created: 21.05.2013
 */

package de.freese.j2ee.jmx;

import java.util.Set;

/**
 * @author Thomas Freese
 */
@FunctionalInterface
public interface IUsageLogMBean
{
    /**
     * @return {@link Set}
     */
    public Set<String> getParameters();
}
