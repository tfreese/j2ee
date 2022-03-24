// Erzeugt: 24.11.2015
package de.freese.wildfly.ejbincdi.cdi;

import de.freese.wildfly.ejbincdi.ejb.ITestServiceBeanLocal;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Named("myNamedBean")
public class NamedBean
{
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NamedBean.class);
    /**
     *
     */
    @EJB
    private ITestServiceBeanLocal serviceBean;

    /**
     * @return ITestServiceBeanLocal
     */
    public ITestServiceBeanLocal getServiceBean()
    {
        return this.serviceBean;
    }

    /**
     * @param s String
     */
    public void log(final String s)
    {
        LOGGER.info(s);
    }

    // /**
    // * @param bean ITestServiceBeanLocal
    // */
    // public void setServiceBean(final ITestServiceBeanLocal serviceBean)
    // {
    // this.serviceBean = serviceBean;
    // }
}
