/**
 * Created: 21.05.2013
 */
package de.freese.j2ee.interceptor;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import javax.inject.Singleton;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import org.slf4j.LoggerFactory;
import de.freese.j2ee.jmx.IUsageLogMBean;
import de.freese.j2ee.jmx.JMXBean;

/**
 * @author Thomas Freese
 */
@Singleton
@Interceptor
@MyLogging
@JMXBean
public class LogInterceptor implements IUsageLogMBean
{
    /**
     * 
     */
    private static final Set<String> parameters = new TreeSet<>();

    // /**
    // * @throws Exception Falls was schief geht.
    // */
    // @PostConstruct
    // public void exportsBean() throws Exception
    // {
    // System.out.println("LogInterceptor.exportsBean()");
    //
    // ObjectName objectName = new ObjectName("LogInterceptor:type=" + getClass().getName());
    //
    // MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    // mBeanServer.registerMBean(this, objectName);
    // }

    /**
     * @see de.freese.j2ee.jmx.IUsageLogMBean#getParameters()
     */
    @Override
    public Set<String> getParameters()
    {
        return LogInterceptor.parameters;
    }

    /**
     * @param ctx {@link InvocationContext}
     * @return Object
     * @throws Exception Falls was schief geht.
     */
    @AroundInvoke
    public Object logNameRequest(final InvocationContext ctx) throws Exception
    {
        Object[] parameters = ctx.getParameters();

        String parameter = Arrays.toString(parameters);

        if (parameter == null)
        {
            parameter = "";
        }

        LogInterceptor.parameters.add(parameter);

        LoggerFactory.getLogger(ctx.getTarget().getClass()).info(parameter);

        return ctx.proceed();
    }
}
