// Created: 21.05.2013
package de.freese.liberty.interceptor.logging;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.slf4j.LoggerFactory;

import de.freese.liberty.jmx.JmxBean;
import de.freese.liberty.jmx.UsageLogMBean;

/**
 * <pre>{@code
 * @POST
 * @Path("METHOD")
 * @Consumes(APPLICATION_OCTET_STREAM)
 * @Produces(APPLICATION_OCTET_STREAM)
 * @MyLogging
 * }</pre>
 *
 * @author Thomas Freese
 */
@Interceptor
@MyLogging
@JmxBean
public class LogInterceptor implements UsageLogMBean {
    // private static final Logger LOGGER = LoggerFactory.getLogger(LogInterceptor.class);

    private static final Set<String> PARAMETERS = new TreeSet<>();

    // @PostConstruct
    // public void exportsBean() throws Exception {
    //     LOGGER.info("LogInterceptor.exportsBean()");
    //
    //     final ObjectName objectName = new ObjectName("LogInterceptor:type=" + getClass().getName());
    //
    //     final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    //     mBeanServer.registerMBean(this, objectName);
    // }

    @Override
    public Set<String> getParameters() {
        return LogInterceptor.PARAMETERS;
    }

    @AroundInvoke
    public Object logNameRequest(final InvocationContext ctx) throws Exception {
        final Object[] params = ctx.getParameters();

        final String parameter = Arrays.toString(params);

        LogInterceptor.PARAMETERS.add(parameter);

        LoggerFactory.getLogger(ctx.getTarget().getClass()).info("{}: {} - {}", getClass().getSimpleName(), ctx.getMethod().getName(), parameter);

        return ctx.proceed();
    }
}
