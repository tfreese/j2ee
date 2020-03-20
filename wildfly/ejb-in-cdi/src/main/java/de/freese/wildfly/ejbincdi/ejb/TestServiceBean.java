// Erzeugt: 24.11.2015
package de.freese.wildfly.ejbincdi.ejb;

import jakarta.ejb.Stateless;

/**
 * @author Thomas Freese
 */
@Stateless
public class TestServiceBean implements ITestServiceBeanLocal
{
    /**
     *
     */
    public TestServiceBean()
    {
        super();
    }

    /**
     * @see de.freese.wildfly.ejbincdi.ejb.ITestServiceBeanLocal#getString(java.lang.String)
     */
    @Override
    public String getString(final String s)
    {
        return s;
    }
}
