/**
 * Created: 18.12.2011
 */

package de.freese.spring.config;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Thomas Freese
 */
@Configuration
@Profile("test")
@PropertySource("classpath:database-test.properties")
public class DatabaseTest
{
    /**
    *
    */
    @Resource
    private Environment env = null;

    /**
     * Erstellt ein neues {@link DatabaseTest} Object.
     */
    public DatabaseTest()
    {
        super();
    }

    /**
     * @return {@link DataSource}
     */
    // @Bean(destroyMethod = "shutdown") // EmbeddedDatabaseBuilder
    @Bean(destroyMethod = "destroy") // SingleConnectionDataSource
    public DataSource dataSource()
    {
        // JndiObjectFactoryBean dataSource = new JndiObjectFactoryBean();
        // dataSource.setJndiName("java:comp/env/jdbc/datasource");

        // EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        // EmbeddedDatabase dataSource = builder.setType(EmbeddedDatabaseType.DERBY).addScript("classpath:database-test.sql").build();

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName(this.env.getProperty("db.driver"));
        dataSource.setUrl(this.env.getProperty("db.url"));
        dataSource.setUsername(this.env.getProperty("db.username"));
        dataSource.setPassword(this.env.getProperty("db.password"));
        dataSource.setSuppressClose(true);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("database-test.sql"));
        populator.execute(dataSource);

        return dataSource;
    }

    /**
     * @return {@link PlatformTransactionManager}
     */
    @Bean
    public PlatformTransactionManager transactionManager()
    {
        return new DataSourceTransactionManager(dataSource());
    }

    // /**
    // * @return {@link PropertyPlaceholderConfigurer}
    // * @throws Exception Falls was schief geht.
    // */
    // @Bean
    // public PropertyPlaceholderConfigurer propertyConfigurer() throws Exception
    // {
    // PropertyPlaceholderConfigurer bean = new PropertyPlaceholderConfigurer();
    // bean.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_FALLBACK);
    // bean.setSearchSystemEnvironment(true);
    // // bean.setProperties(propertiesDB());
    // bean.setLocation(new ClassPathResource("classpath:database-test.properties"));
    //
    // return bean;
    // }
}
