// Created: 20.05.2018
package de.freese.j2ee.liberty.config;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import de.freese.j2ee.liberty.config.service.MyService;

/**
 * @author Thomas Freese
 */
@Path("service")
public class MyRestFacade extends AbstractBean {
    @Resource(lookup = "java:comp/DefaultManagedExecutorService")
    private ExecutorService executorService;
    
    @EJB
    private MyService serviceBean;

    public MyRestFacade() {
        super();

        // ManagedThreadFactory threadFactory =
        // (ManagedThreadFactory) new InitialContext().lookup(
        // "java:comp/DefaultManagedThreadFactory");

        // <managedExecutorService jndiName="concurrent/executor">
        // <concurrencyPolicy max="2" maxQueueSize="3"
        // runIfQueueFull="false" maxWaitForEnqueue="0" />
        // </managedExecutorService>
    }

    /**
     * http://localhost:9080/config/rest/service/jndi
     */
    @GET
    @Path("jndi")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getJndiTree() throws Exception {
        List<String> names = Arrays.asList("", "java:global", "java:app", "java:module", "java:comp");

        Map<String, Object> map = new TreeMap<>();
        InitialContext initialContext = new InitialContext();

        for (String name : names) {
            Context context = (Context) initialContext.lookup(name);
            map.put(name, dumpContextNameClassPair(context));
            // map.put(name, dumpContextBinding(context));
            context.close();
        }

        initialContext.close();

        return map;
    }

    /**
     * http://localhost:9080/config/rest/service/sysdate
     */
    @GET
    @Path("sysdate")
    @Produces(MediaType.APPLICATION_JSON)
    public LocalDateTime getSysdate() throws SQLException {
        getLogger().info("getSysdate");

        return getServiceBean().getSysDate();
    }

    /**
     * http://localhost:9080/config/rest/service/properties
     */
    @GET
    @Path("properties")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getSystemProperties() {
        getLogger().info("getSystemProperties");

        JsonObjectBuilder builder = Json.createObjectBuilder();
        Map<String, String> properties = getServiceBean().getSystemProperties();

        properties.forEach(builder::add);

        return builder.build();
    }

    @Override
    @PostConstruct
    public void postConstruct() {
        super.postConstruct();

        this.executorService.execute(() -> getLogger().info("postConstruct with ManagedExecutorService"));
    }

    private Map<String, Object> dumpContextBinding(final Context ctx) throws Exception {
        NamingEnumeration<Binding> enumeration = ctx.listBindings("");
        Map<String, Object> map = new TreeMap<>();

        while (enumeration.hasMoreElements()) {
            Binding binding = enumeration.next();
            String name = binding.getName();

            Object tmp = null;

            try {
                tmp = binding.getObject();
            }
            catch (Exception ex) {
                tmp = binding.getClassName();
            }

            if (tmp instanceof Context subContext) {
                map.put(name, dumpContextBinding(subContext));
            }
            else {
                map.put(name, tmp.toString());
            }
        }

        return map;
    }

    private Map<String, Object> dumpContextNameClassPair(final Context ctx) throws Exception {
        NamingEnumeration<NameClassPair> enumeration = ctx.list("");
        Map<String, Object> map = new TreeMap<>();

        while (enumeration.hasMoreElements()) {
            NameClassPair pair = enumeration.next();
            String name = pair.getName();
            String className = pair.getClassName();

            boolean isSubContext = false;

            try {
                isSubContext = Class.forName(className).isAssignableFrom(Context.class);
            }
            catch (Exception ex) {
                // Ignore
            }

            if (isSubContext) {
                Context subContext = (Context) ctx.lookup(name);

                map.put(name, dumpContextNameClassPair(subContext));
            }
            else {
                try {
                    map.put(name, ctx.lookup(name).toString());
                }
                catch (Exception ex) {
                    map.put(name, className);
                }
            }
        }

        return map;
    }

    private MyService getServiceBean() {
        if (this.serviceBean == null) {
            try {
                this.serviceBean = Utils.lookup("java:module/MyServiceBean!de.freese.j2ee.liberty.config.service.MyService", MyService.class);
                //                this.serviceBean = Utils.lookup("java:global/liberty-config/MyServiceBean!de.freese.j2ee.liberty.config.service.MyService", MyService.class);
                //                this.serviceBean = Utils.ejb(MyServiceBean.class);
            }
            catch (RuntimeException ex) {
                getLogger().error(ex.getMessage(), ex.getCause());
            }
        }

        return this.serviceBean;
    }
}
