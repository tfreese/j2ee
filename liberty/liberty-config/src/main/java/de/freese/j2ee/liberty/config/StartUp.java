/**
 * Created: 18.05.2018
 */

package de.freese.j2ee.liberty.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;
import de.freese.j2ee.liberty.config.service.MyService;

/**
 * @author Thomas Freese
 */
@Startup
@Singleton
@LocalBean
public class StartUp extends AbstractBean
{
    /**
    *
    */
    @Resource(lookup = "jdbc/hsqldbDS")
    private DataSource dataSource = null;

    /**
     *
     */
    @EJB
    private MyService serviceBean = null;

    /**
     * No-View Beans (ohne Interface) funktionieren komischer Weise nicht !
     */
    // @EJB
    private NoViewBean noViewBean = null;

    /**
     * Erstellt ein neues {@link StartUp} Object.
     */
    public StartUp()
    {
        super();
    }

    /**
     * @see de.freese.j2ee.liberty.config.AbstractBean#postConstruct()
     */
    @Override
    @PostConstruct
    public void postConstruct()
    {
        super.postConstruct();

        query(this.dataSource, "select 1 from INFORMATION_SCHEMA.SYSTEM_USERS");

        try
        {
            getLogger().info("Sysdate: {}", this.serviceBean.getSysDate());
        }
        catch (Exception ex)
        {
            getLogger().error(null, ex);
        }

        if (this.noViewBean == null)
        {
            getLogger().warn("NoViewBean is null, try ejb lookup with java:module/TestBean");

            try
            {
                this.noViewBean = Utils.ejb(NoViewBean.class);

                if (this.noViewBean != null)
                {
                    getLogger().warn("NoViewBean lookup successfull");
                }
            }
            catch (RuntimeException rex)
            {
                getLogger().error(rex.getMessage());
            }
        }

        if (this.noViewBean == null)
        {
            getLogger().warn("NoViewBean is null, try inject lookup");

            try
            {
                this.noViewBean = Utils.inject(NoViewBean.class);

                if (this.noViewBean != null)
                {
                    getLogger().warn("NoViewBean lookup successfull");
                }
            }
            catch (RuntimeException rex)
            {
                getLogger().error(rex.getMessage());
            }
        }

        try
        {
            getLogger().info("NoViewBean: {}", this.noViewBean.getValue());
        }
        catch (Exception ex)
        {
            getLogger().error(ex.getMessage());
        }
    }

    /**
     * @param dataSource {@link DataSource}
     * @param sql String
     */
    private void query(final DataSource dataSource, final String sql)
    {
        try
        {
            int value = 0;

            try (Connection con = dataSource.getConnection();
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql))
            {
                rs.next();
                value = rs.getInt(1);
            }

            getLogger().info("Value: {}", value);
        }
        catch (Exception ex)
        {
            getLogger().error(null, ex);
        }
    }
}
