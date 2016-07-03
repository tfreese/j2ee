/**
 * Created: 11.12.2011
 */

package de.freese.spring.config.dao;

import java.util.List;

/**
 * @author Thomas Freese
 */
public interface ITestDAO
{
    /**
     * @return {@link List}
     */
    public List<String> loadData();
}
