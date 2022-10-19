// Created: 21.06.2018
package de.freese.j2ee.liberty.spring;

import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jndi.JndiTemplate;

/**
 * @author Thomas Freese
 */
@Configuration
@Profile("liberty")
public class ConfigLiberty
{
    @Resource
    private Environment env;

    @Bean
    public DataSource dataSource() throws NamingException
    {
        // JndiObjectFactoryBean
        // JndiDataSourceLookup
        return (DataSource) new JndiTemplate().lookup(this.env.getProperty("datasource.jndi.name"));
    }
}
