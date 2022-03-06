// Created: 21.06.2018
package de.freese.j2ee.liberty.spring;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * @author Thomas Freese
 */
@Configuration
@Profile("tomcat")
public class ConfigTomcat
{
    /**
     *
     */
    @Resource
    private Environment env;

    /**
     * Erstellt ein neues {@link ConfigTomcat} Object.
     */
    public ConfigTomcat()
    {
        super();
    }

    /**
     * @return {@link DataSource}
     *
     * @throws NamingException Falls was schief geht.
     */
    @Bean
    public DataSource dataSource() throws NamingException
    {
        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setURL("jdbc:hsqldb:mem:liberty-spring");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        return dataSource;
    }
}
