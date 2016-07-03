/**
 * Created: 11.12.2011
 */

package de.freese.spring.config;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import de.freese.spring.config.dao.ITestDAO;
import de.freese.spring.config.dao.TestDAO;

/**
 * @author Thomas Freese
 */
@Configuration
@Import(
{
        DatabaseTest.class, DatabaseProd.class
})
@EnableTransactionManagement
public class ApplicationConfig
{
    /**
    *
    */
    @Resource
    private DataSource dataSource = null;

    /**
    *
    */
    @Value("${db.name}")
    // @Value("#{propertiesDB['db.name']}")
    private String dbName = null;

    /**
    *
    */
    @Resource
    private Environment env = null;

    // /**
    // *
    // */
    // @Resource(name = "propertiesDB")
    // private Properties propertiesDB = null;

    /**
     * Erstellt ein neues {@link ApplicationConfig} Object.
     */
    public ApplicationConfig()
    {
        super();
    }

    /**
     * @return {@link ITestDAO}
     */
    @Bean
    // @Bean(initMethod = "startup"), destroyMethod = "shutdown")
    public ITestDAO testDAO()
    {
        // String dbName = this.env.getProperty("db.name");

        return new TestDAO(this.dataSource, this.dbName);
    }
}
