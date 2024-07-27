// Created: 20.05.2018
package de.freese.j2ee.liberty.config.rest;

import java.lang.invoke.MethodHandles;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import de.freese.j2ee.liberty.config.AbstractBean;
import de.freese.j2ee.liberty.config.Utils;
import de.freese.j2ee.liberty.config.service.MyService;

/**
 * @author Thomas Freese
 */
@Path("service")
public class MyRestFacade extends AbstractBean {

    @EJB
    private MyService serviceBean;

    public MyRestFacade() {
        super();

        // final ManagedThreadFactory threadFactory =(ManagedThreadFactory) new InitialContext().lookup("java:comp/DefaultManagedThreadFactory");

        // <managedExecutorService jndiName="concurrent/executor">
        // <concurrencyPolicy max="2" maxQueueSize="3"
        // runIfQueueFull="false" maxWaitForEnqueue="0" />
        // </managedExecutorService>
    }

    /**
     * <a href="http://localhost:9080/config/rest/service/jndi">jndi</a>
     */
    @GET
    @Path("jndi")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getJndiTree() throws Exception {
        final List<String> names = Arrays.asList("", "java:global", "java:app", "java:module", "java:comp");

        final Map<String, Object> map = new TreeMap<>();
        final InitialContext initialContext = new InitialContext();

        for (String name : names) {
            final Context context = (Context) initialContext.lookup(name);
            map.put(name, dumpContextNameClassPair(context));
            // map.put(name, dumpContextBinding(context));
            context.close();
        }

        initialContext.close();

        return map;
    }

    /**
     * <a href="http://localhost:9080/config/rest/service/sysdate">sysdate</a>
     */
    @GET
    @Path("sysdate")
    @Produces(MediaType.APPLICATION_JSON)
    public LocalDateTime getSysdate() throws SQLException {
        getLogger().info("getSysdate");

        return getServiceBean().getSysDate();
    }

    /**
     * <a href="http://localhost:9080/config/rest/service/properties">properties</a>
     */
    @GET
    @Path("properties")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getSystemProperties() {
        getLogger().info("getSystemProperties");

        final JsonObjectBuilder builder = Json.createObjectBuilder();
        final Map<String, String> properties = getServiceBean().getSystemProperties();

        properties.forEach(builder::add);

        return builder.build();
    }

    @Override
    @PostConstruct
    public void postConstruct() {
        super.postConstruct();
    }

    private Map<String, Object> dumpContextBinding(final Context ctx) throws Exception {
        final NamingEnumeration<Binding> enumeration = ctx.listBindings("");
        final Map<String, Object> map = new TreeMap<>();

        while (enumeration.hasMoreElements()) {
            final Binding binding = enumeration.next();
            final String name = binding.getName();

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

        enumeration.close();

        return map;
    }

    private Map<String, Object> dumpContextNameClassPair(final Context ctx) throws Exception {
        final NamingEnumeration<NameClassPair> enumeration = ctx.list("");
        final Map<String, Object> map = new TreeMap<>();

        while (enumeration.hasMoreElements()) {
            final NameClassPair pair = enumeration.next();
            final String name = pair.getName();
            final String className = pair.getClassName();

            boolean isSubContext = false;

            try {
                isSubContext = MethodHandles.publicLookup().findClass(className).isAssignableFrom(Context.class);
                // isSubContext = Class.forName(className).isAssignableFrom(Context.class);
            }
            catch (Exception ex) {
                // Ignore
            }

            if (isSubContext) {
                final Context subContext = (Context) ctx.lookup(name);

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

        enumeration.close();

        return map;
    }

    private MyService getServiceBean() {
        if (this.serviceBean == null) {
            try {
                this.serviceBean = Utils.lookup("java:module/MyServiceBean!de.freese.j2ee.liberty.config.service.MyService", MyService.class);
                // this.serviceBean = Utils.lookup("java:global/liberty-config/MyServiceBean!de.freese.j2ee.liberty.config.service.MyService", MyService.class);
                // this.serviceBean = Utils.ejb(MyServiceBean.class);
            }
            catch (RuntimeException ex) {
                getLogger().error(ex.getMessage(), ex.getCause());
            }
        }

        return this.serviceBean;
    }
}
