// Created: 21.06.2018
package de.freese.j2ee.liberty.spring;

import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.annotation.Resource;

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
    @Resource
    private Environment env;

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
