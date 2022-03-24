// Created: 02.06.2018
package de.freese.j2ee.liberty.cache;

import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Startup
@Singleton
@Lock(LockType.READ)
public class HazelcastInitializer
{
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HazelcastInitializer.class);
    /**
     *
     */
    private static HazelcastInstance hazelcastInstance;

    /**
     * @return {@link HazelcastInstance}
     */
    public static HazelcastInstance getHazelcastInstance()
    {
        return hazelcastInstance;
    }

    /**
     *
     */
    @Resource(lookup = "java:comp/DefaultManagedExecutorService")
    private ExecutorService executorService;

    /**
     *
     */
    @PostConstruct
    public void postConstruct()
    {
        this.executorService.execute(this::initHazelcast);
    }

    /**
     *
     */
    private void initHazelcast()
    {
        LOGGER.info("init hazelcast");

        if (hazelcastInstance != null)
        {
            LOGGER.info("hazelcast already initilized");
        }
        else
        {
            URL configUrl = null;

            if (configUrl == null)
            {
                LOGGER.info("try with ClassLoader.getSystemResource");

                configUrl = ClassLoader.getSystemResource("hazelcast.xml");
            }

            if (configUrl == null)
            {
                LOGGER.info("try with Thread.currentThread().getContextClassLoader().getResource");

                configUrl = Thread.currentThread().getContextClassLoader().getResource("hazelcast.xml");
            }

            if (configUrl == null)
            {
                LOGGER.info("try with RestApplication.class.getClassLoader().getResource");

                configUrl = RestApplication.class.getClassLoader().getResource("hazelcast.xml");
            }

            if (configUrl == null)
            {
                LOGGER.info("try with Paths.get(System.getProperty(\"server.config.dir\"), \"apps_resources\", \"hazelcast.xml\")");

                try
                {
                    configUrl = Paths.get(System.getProperty("server.config.dir"), "apps_resources", "hazelcast.xml").toUri().toURL();
                }
                catch (Exception ex)
                {
                    LOGGER.error(ex.getMessage());
                }
            }

            try
            {
                Config config = new XmlConfigBuilder(configUrl).build();
                // Config config = new XmlConfigBuilder().build();
                hazelcastInstance = Hazelcast.newHazelcastInstance(config);
            }
            catch (Exception ex)
            {
                LOGGER.error(ex.getMessage());
            }
        }
    }
}
