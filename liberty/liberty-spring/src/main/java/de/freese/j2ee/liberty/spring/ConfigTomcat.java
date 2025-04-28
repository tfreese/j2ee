// Created: 21.06.2018
package de.freese.j2ee.liberty.spring;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Thomas Freese
 */
@Configuration
@Profile("tomcat")
public class ConfigTomcat {
    @Bean
    public DataSource dataSource() {
        final HikariConfig hikariConfig = new HikariConfig();
        // hikariConfig.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        // hikariConfig.setJdbcUrl("jdbc:hsqldb:mem:liberty-spring;shutdown=true");
        hikariConfig.setDriverClassName("org.h2.jdbcx.JdbcDataSource");
        hikariConfig.setJdbcUrl("jdbc:h2:mem:liberty-spring;DB_CLOSE_DELAY=0;DB_CLOSE_ON_EXIT=true");
        hikariConfig.setUsername("sa");
        hikariConfig.setPassword("");
        hikariConfig.setPoolName("hikari-" + hikariConfig.getJdbcUrl());
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setMaximumPoolSize(3);
        hikariConfig.setAutoCommit(false);
        hikariConfig.setTransactionIsolation("TRANSACTION_READ_COMMITTED");

        return new HikariDataSource(hikariConfig);

        // H2
        // final JdbcConnectionPool pool = JdbcConnectionPool.create("jdbc:h2:mem:liberty-spring;DB_CLOSE_DELAY=0;DB_CLOSE_ON_EXIT=true", "sa", null);
        // pool.setMaxConnections(3);

        // HSQLDB
        // final JDBCPool pool = new JDBCPool(3);
        // pool.setURL("jdbc:hsqldb:mem:liberty-spring;shutdown=true");
        // pool.setUser("sa");
        // pool.setPassword("");
    }
}
