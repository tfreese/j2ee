// Created: 21.05.2013
package de.freese.j2ee.interceptor;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import jakarta.ejb.Singleton;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.slf4j.LoggerFactory;

import de.freese.j2ee.jmx.JmxBean;
import de.freese.j2ee.jmx.UsageLogMBean;

/**
 * @author Thomas Freese
 */
@Singleton
@Interceptor
@MyLogging
@JmxBean
public class LogInterceptor implements UsageLogMBean {
    private static final Set<String> parameters = new TreeSet<>();

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
     * @see UsageLogMBean#getParameters()
     */
    @Override
    public Set<String> getParameters() {
        return LogInterceptor.parameters;
    }

    @AroundInvoke
    public Object logNameRequest(final InvocationContext ctx) throws Exception {
        Object[] params = ctx.getParameters();

        String parameter = Arrays.toString(params);

        if (parameter == null) {
            parameter = "";
        }

        LogInterceptor.parameters.add(parameter);

        LoggerFactory.getLogger(ctx.getTarget().getClass()).info(parameter);

        return ctx.proceed();
    }
}
