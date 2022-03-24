// Created: 20.05.2018
package de.freese.j2ee.liberty.cache;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.hazelcast.map.IMap;
import com.ibm.websphere.cache.DistributedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Die DistributedMap ist nur per lookup erreichbar !<br>
 * <br>
 * -H "Accept: text/plain"<br>
 * curl -X GET localhost:9091/cache;<br>
 * curl -X PUT localhost:9091/cache?value=Hello%20World;<br>
 * <br>
 * Liberty-Modul:<br>
 * Download: https://github.com/hazelcast/hazelcast-dynacache/releases/download/v0.2/hazelcast-dynacache-0.2.esa<br>
 * <br>
 * Selbstbau: https://blog.hazelcast.com/hazelcast-ibm-dynacache-provider<br>
 * git clone -b bundle https://github.com/emre-aydin/hazelcast.git<br>
 * mvn clean install -DskipTests=true<br>
 * git clone https://github.com/hazelcast/hazelcast-dynacache.git<br>
 * <br>
 * Als Feature installieren: $LIBERTY_HOME/bin/featureManager install hazelcast-dynacache-0.2.esa <feature>usr:hazelcast-dynacache</feature>
 *
 * @author Thomas Freese
 */
@Path("/")
public class MyRestFacade
{
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MyRestFacade.class);

    /**
     * Die DistributedMap ist nur per lookup erreichbar !
     */
    // @Resource(lookup = "services/cache/distributedmap") // DefaultCache
    // @Resource(lookup = "cache/test")
    private DistributedMap cache = null;

    /**
     * http://localhost:9090/cache
     *
     * @return {@link Date}
     */
    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String getValue()
    {
        String value = (String) getDistributedMap().get("key");
        LOGGER.info("Value from DistributedMap: {}", value);

        value = getHazelcastMap().get("key");
        LOGGER.info("Value from Hazelcast: {}", value);

        return value;
    }

    /**
     * Die DistributedMap ist nur per lookup erreichbar !
     */
    @PostConstruct
    public void postConstruct()
    {
        LOGGER.info("test cache lookups");

        DistributedMap map = null;

        // "cache/test",
        for (String jndiName : Arrays.asList("baseCache", "cache/test", "services/cache/distributedmap"))
        {
            LOGGER.info("try lookup with {}", jndiName);

            try
            {
                Context context = new InitialContext();
                map = (DistributedMap) context.lookup(jndiName);

                if (map != null)
                {
                    LOGGER.info("lookup successfull with {}", jndiName);
                }
            }
            catch (NamingException nex)
            {
                LOGGER.error(nex.getMessage());
            }
        }

        if (map == null)
        {
            LOGGER.info("cache is already null !");
        }
    }

    /**
     * http://localhost:9090/cache
     *
     * @param value String
     */
    @PUT
    @Path("/")
    @Consumes(MediaType.TEXT_PLAIN)
    public void putValue(@QueryParam("value") final String value)
    {
        getDistributedMap().put("key", value);
        getHazelcastMap().put("key", value);
    }

    /**
     * Die DistributedMap ist nur per lookup erreichbar !
     *
     * @return {@link DistributedMap}
     */
    private DistributedMap getDistributedMap()
    {
        if (this.cache == null)
        {
            for (String jndiName : Arrays.asList("cache/test", "services/cache/distributedmap"))
            {
                LOGGER.info("try lookup with {}", jndiName);

                try
                {
                    Context context = new InitialContext();
                    this.cache = (DistributedMap) context.lookup(jndiName);

                    if (this.cache != null)
                    {
                        LOGGER.info("lookup successfull with {}", jndiName);
                        break;
                    }
                }
                catch (NamingException nex)
                {
                    LOGGER.error(nex.getMessage());
                }
            }

            if (this.cache == null)
            {
                LOGGER.info("Cache is already null !");
            }
        }

        return this.cache;
    }

    /**
     * @return {@link IMap}
     */
    private IMap<String, String> getHazelcastMap()
    {
        return HazelcastInitializer.getHazelcastInstance().getMap("test");
    }
}
