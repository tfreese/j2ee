// Created: 21.05.2013
package de.freese.liberty.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Startup
@Singleton
public class JmxBeanExtension {
    private static final Logger LOGGER = LoggerFactory.getLogger(JmxBeanExtension.class);

    // @Inject
    // private LogInterceptor interceptor;

    @Inject
    @JmxBean
    // @Any
    private Instance<Object> jmxBeans;

    @PostConstruct
    public void exportsBean() {
        LOGGER.info("JmxBeanExtension.exportsBean()");

        final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        if (jmxBeans == null) {
            LOGGER.info("No jmxBeans, returning");
            return;
        }

        try {
            for (Object bean : jmxBeans) {
                final Class<?> beanClass = bean.getClass();
                // final Object bean = this.interceptor;
                // final Class<?> beanClass = LogInterceptor.class;
                // final String annotationValue = beanClass.getAnnotation(JmxBean.class).objectName();
                final String annotationValue = beanClass.getSimpleName();
                ObjectName objectName = null;

                if (annotationValue.isEmpty()) {
                    objectName = new ObjectName(beanClass.getName());
                }
                else {
                    objectName = new ObjectName(annotationValue + ":type=" + beanClass.getName());
                }

                mBeanServer.registerMBean(bean, objectName);

                LOGGER.info("Registered {}", objectName);
            }
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }
}
