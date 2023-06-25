// Created: 20.05.2018
package de.freese.j2ee.liberty.config;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

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
    @EJB
    private MyService serviceBean;

    /**
     * http://localhost:9080/config/rest/service/sysdate
     */
    @GET
    @Path("sysdate")
    @Produces(MediaType.APPLICATION_JSON)
    public Date getSysdate() throws SQLException {
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

    private MyService getServiceBean() {
        if (this.serviceBean == null) {
            try {
                this.serviceBean = Utils.lookup("java:global/liberty-config/MyServiceBean!de.freese.j2ee.liberty.config.service.MyService", MyService.class);
                // this.serviceBean = Utils.lookup("java:module/MyServiceBean!de.freese.j2ee.liberty.config.service.MyService");
                // this.serviceBean = Utils.ejb(MyServiceBean.class);
            }
            catch (RuntimeException ex) {
                getLogger().error(ex.getMessage(), ex.getCause());
            }
        }

        return this.serviceBean;
    }
}
