// Created: 24.05.2018
package de.freese.j2ee.liberty.config.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.sql.DataSource;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;

import de.freese.j2ee.liberty.config.AbstractBean;

/**
 * @author Thomas Freese
 */
@Stateless
public class MyServiceBean extends AbstractBean implements MyService {
    @Resource(lookup = "jdbc/hsqldbDS")
    private DataSource dataSource;

    @Override
    public LocalDateTime getSysDate() throws SQLException {
        getLogger().info("getSysDate");

        //        String sql = "VALUES (CURRENT_DATE)";
        final String sql = "VALUES (CURRENT_TIMESTAMP)";
        LocalDateTime localDateTime = null;

        try (Connection con = this.dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            localDateTime = rs.getTimestamp(1).toLocalDateTime();
        }

        return localDateTime;
    }

    @Override
    public Map<String, String> getSystemProperties() {
        getLogger().info("getSystemProperties");

        final Map<String, String> map = new TreeMap<>();

        final Properties properties = System.getProperties();
        properties.forEach((key, value) -> map.put((String) key, (String) value));

        return map;
    }

    @Override
    @PostConstruct
    public void postConstruct() {
        super.postConstruct();

        if (this.dataSource == null) {
            getLogger().error("Datasource is null !");
        }
    }
}
