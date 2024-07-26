// Created: 21.06.2018
package de.freese.j2ee.liberty.spring;

import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.annotation.Resource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * @author Thomas Freese
 */
@Configuration
@Profile("tomcat")
public class ConfigTomcat {
    @Resource
    private Environment env;

    @Bean
    public DataSource dataSource() throws NamingException {
        final JdbcConnectionPool pool = JdbcConnectionPool.create("jdbc:h2:mem:liberty-spring;DB_CLOSE_DELAY=0;DB_CLOSE_ON_EXIT=true", "sa", null);
        pool.setMaxConnections(3);

        // final JDBCPool pool = new JDBCPool(3);
        // pool.setURL("jdbc:hsqldb:mem:liberty-spring;shutdown=true");
        // pool.setUser("sa");
        // pool.setPassword("");

        return pool;
    }
}
