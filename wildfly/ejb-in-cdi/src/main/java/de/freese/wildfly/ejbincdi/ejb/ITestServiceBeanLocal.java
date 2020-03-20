// Erzeugt: 24.11.2015
package de.freese.wildfly.ejbincdi.ejb;

import jakarta.ejb.Local;

/**
 * @author Thomas Freese
 */
@Local
public interface ITestServiceBeanLocal
{
    /**
     * @param s String
     *            <p>
     * @return String
     */
    public String getString(String s);
}
