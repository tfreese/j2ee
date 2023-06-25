// Created: 18.05.2018
package de.freese.j2ee.liberty.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import de.freese.j2ee.liberty.config.service.MyService;

/**
 * @author Thomas Freese
 */
@Startup
@Singleton
public class StartUp extends AbstractBean {
    @Resource(lookup = "jdbc/hsqldbDS")
    private DataSource dataSource;

    /**
     * No-View Beans (without Interface) doesn't work ?!
     */
    //    @EJB
    private NoViewBean noViewBean;

    @EJB
    private MyService serviceBean;

    /**
     * @see de.freese.j2ee.liberty.config.AbstractBean#postConstruct()
     */
    @Override
    @PostConstruct
    public void postConstruct() {
        super.postConstruct();

        query(this.dataSource, "select 1 from INFORMATION_SCHEMA.SYSTEM_USERS");

        try {
            getLogger().info("Sysdate: {}", this.serviceBean.getSysDate());
        }
        catch (Exception ex) {
            getLogger().error(ex.getMessage(), ex);
        }

        if (this.noViewBean == null) {
            getLogger().warn("NoViewBean is null, try ejb lookup");

            try {
                this.noViewBean = Utils.ejb(NoViewBean.class);

                if (this.noViewBean != null) {
                    getLogger().warn("NoViewBean lookup successfully");
                }
            }
            catch (RuntimeException rex) {
                getLogger().error(rex.getMessage());
            }
        }

        if (this.noViewBean == null) {
            getLogger().warn("NoViewBean is null, try inject lookup");

            try {
                this.noViewBean = Utils.inject(NoViewBean.class);

                if (this.noViewBean != null) {
                    getLogger().warn("NoViewBean lookup successfully");
                }
            }
            catch (RuntimeException rex) {
                getLogger().error(rex.getMessage());
            }
        }

        if (this.noViewBean == null) {
            getLogger().warn("NoViewBean is null, try jndi lookup");

            try {
                this.noViewBean = Utils.lookup("java:module/NoViewBean!de.freese.j2ee.liberty.config.NoViewBean", NoViewBean.class);

                if (this.noViewBean != null) {
                    getLogger().warn("NoViewBean lookup successfully");
                }
            }
            catch (RuntimeException rex) {
                getLogger().error(rex.getMessage());
            }
        }

        try {
            getLogger().info("NoViewBean: {}", this.noViewBean.getValue());
        }
        catch (Exception ex) {
            getLogger().error(ex.getMessage());
        }
    }

    private void query(final DataSource dataSource, final String sql) {
        try {
            int value = 0;

            try (Connection con = dataSource.getConnection();
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                rs.next();
                value = rs.getInt(1);
            }

            getLogger().info("Value: {}", value);
        }
        catch (Exception ex) {
            getLogger().error(ex.getMessage(), ex);
        }
    }
}
