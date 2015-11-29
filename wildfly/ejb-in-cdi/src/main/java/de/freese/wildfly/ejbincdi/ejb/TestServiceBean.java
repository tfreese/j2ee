// Erzeugt: 24.11.2015
package de.freese.wildfly.ejbincdi.ejb;

import javax.ejb.Stateless;

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
     * @see de.efreest.arquillianTest.ITestServiceBeanLocal#getString(java.lang.String)
     */
    @Override
    public String getString(final String s)
    {
        return s;
    }
}
