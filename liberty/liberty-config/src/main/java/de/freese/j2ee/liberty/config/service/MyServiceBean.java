// Created: 24.05.2018
package de.freese.j2ee.liberty.config.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import de.freese.j2ee.liberty.config.AbstractBean;

/**
 * @author Thomas Freese
 */
@Stateless
// @LocalBean
// @Local(MyService.class)
public class MyServiceBean extends AbstractBean implements MyService
{
    /**
     *
     */
    @Resource(lookup = "jdbc/hsqldbDS")
    private DataSource dataSource;

    /**
     * @see de.freese.j2ee.liberty.config.service.MyService#getSysDate()
     */
    @Override
    public Date getSysDate() throws SQLException
    {
        getLogger().info("getSysDate");

        String sql = "VALUES (CURRENT_DATE)";
        Date date = null;

        try (Connection con = this.dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            rs.next();
            date = rs.getDate(1);
        }

        return date;
    }

    /**
     * @see de.freese.j2ee.liberty.config.service.MyService#getSystemProperties()
     */
    @Override
    public Map<String, String> getSystemProperties()
    {
        getLogger().info("getSystemProperties");

        Map<String, String> map = new TreeMap<>();

        Properties properties = System.getProperties();
        properties.forEach((key, value) -> map.put((String) key, (String) value));

        return map;
    }

    /**
     * @see de.freese.j2ee.liberty.config.AbstractBean#postConstruct()
     */
    @Override
    @PostConstruct
    public void postConstruct()
    {
        super.postConstruct();

        if (this.dataSource == null)
        {
            getLogger().error("Datasource is null !");
        }
    }
}
