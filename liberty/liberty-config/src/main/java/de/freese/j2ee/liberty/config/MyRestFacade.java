/**
 * Created: 20.05.2018
 */

package de.freese.j2ee.liberty.config;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import de.freese.j2ee.liberty.config.service.MyService;

/**
 * @author Thomas Freese
 */
// @RequestScoped
@Path("service")
public class MyRestFacade extends AbstractBean
{
    /**
     *
     */
    @EJB
    private MyService serviceBean = null;

    /**
     * Erstellt ein neues {@link MyRestFacade} Object.
     */
    public MyRestFacade()
    {
        super();
    }

    /**
     * @return {@link MyService}
     */
    private MyService getServiceBean()
    {
        if (this.serviceBean == null)
        {
            try
            {
                this.serviceBean = Utils.lookup("java:global/liberty-config/MyServiceBean!de.freese.j2ee.liberty.config.service.MyService");
                // this.serviceBean = Utils.lookup("java:module/MyServiceBean!de.freese.j2ee.liberty.config.service.MyService");
                // this.serviceBean = Utils.ejb(MyServiceBean.class);
            }
            catch (RuntimeException rex)
            {
                getLogger().error(null, rex.getCause());
            }
        }

        return this.serviceBean;
    }

    /**
     * http://localhost:9080/config/rest/service/sysdate
     *
     * @return {@link Date}
     * @throws SQLException Falls was schief geht.
     */
    @GET
    @Path("sysdate")
    @Produces(MediaType.APPLICATION_JSON)
    public Date getSysdate() throws SQLException
    {
        getLogger().info("getSysdate");

        Date date = getServiceBean().getSysDate();

        return date;
    }

    /**
     * http://localhost:9080/config/rest/service/properties
     *
     * @return {@link JsonObject}
     */
    @GET
    @Path("properties")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getSystemProperties()
    {
        getLogger().info("getSystemProperties");

        JsonObjectBuilder builder = Json.createObjectBuilder();
        Map<String, String> properties = getServiceBean().getSystemProperties();

        properties.forEach(builder::add);

        return builder.build();
    }
}
