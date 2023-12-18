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
    private static final Set<String> PARAMETERS = new TreeSet<>();

    // @PostConstruct
    // public void exportsBean() throws Exception
    // {
    // System.out.println("LogInterceptor.exportsBean()");
    //
    // final ObjectName objectName = new ObjectName("LogInterceptor:type=" + getClass().getName());
    //
    // final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    // mBeanServer.registerMBean(this, objectName);
    // }

    @Override
    public Set<String> getParameters() {
        return LogInterceptor.PARAMETERS;
    }

    @AroundInvoke
    public Object logNameRequest(final InvocationContext ctx) throws Exception {
        final Object[] params = ctx.getParameters();

        String parameter = Arrays.toString(params);

        if (parameter == null) {
            parameter = "";
        }

        LogInterceptor.PARAMETERS.add(parameter);

        LoggerFactory.getLogger(ctx.getTarget().getClass()).info(parameter);

        return ctx.proceed();
    }
}
