// Created: 18.05.2018
package de.freese.j2ee.liberty.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

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

    // // (unitName = "jpa_unit")
    // @PersistenceContext
    // private EntityManager entityManager;

    @Resource(lookup = "java:comp/DefaultManagedExecutorService")
    private ExecutorService executorService;
    /**
     * No-View Beans (without Interface) doesn't work ?!
     * Only if configured in ejb-jar.xml
     */
    //@EJB
    private NoViewBean noViewBean;

    @Resource(lookup = "java:comp/DefaultManagedScheduledExecutorService")
    private ScheduledExecutorService scheduledExecutorService;

    @EJB
    private MyService serviceBean;

    @Override
    @PostConstruct
    public void postConstruct() {
        super.postConstruct();

        executorService.execute(() -> getLogger().info("postConstruct with DefaultManagedExecutorService"));
        scheduledExecutorService.execute(() -> getLogger().info("postConstruct with DefaultManagedScheduledExecutorService"));

        query(dataSource, "select 1 from INFORMATION_SCHEMA.SYSTEM_USERS");

        // final Object hibernateResult = entityManager.createNativeQuery("VALUES (CURRENT_TIMESTAMP)").getSingleResult();
        // getLogger().info("HibernateResult: {}", hibernateResult);

        try {
            getLogger().info("Sysdate: {}", serviceBean.getSysDate());
        }
        catch (Exception ex) {
            getLogger().error(ex.getMessage(), ex);
        }

        if (noViewBean == null) {
            getLogger().warn("NoViewBean is null, try ejb lookup");

            try {
                noViewBean = Utils.ejb(NoViewBean.class);

                if (noViewBean != null) {
                    getLogger().warn("NoViewBean lookup successfully");
                }
            }
            catch (RuntimeException rex) {
                getLogger().error(rex.getMessage());
            }
        }

        if (noViewBean == null) {
            getLogger().warn("NoViewBean is null, try inject lookup");

            try {
                noViewBean = Utils.inject(NoViewBean.class);

                if (noViewBean != null) {
                    getLogger().warn("NoViewBean lookup successfully");
                }
            }
            catch (RuntimeException rex) {
                getLogger().error(rex.getMessage());
            }
        }

        if (noViewBean == null) {
            getLogger().warn("NoViewBean is null, try jndi lookup");

            try {
                noViewBean = Utils.lookup("java:module/NoViewBean!de.freese.j2ee.liberty.config.NoViewBean", NoViewBean.class);

                if (noViewBean != null) {
                    getLogger().warn("NoViewBean lookup successfully");
                }
            }
            catch (RuntimeException rex) {
                getLogger().error(rex.getMessage());
            }
        }

        try {
            getLogger().info("NoViewBean: {}", noViewBean.getValue());
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
