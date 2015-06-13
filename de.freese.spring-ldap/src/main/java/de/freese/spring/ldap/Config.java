/**
 * Created on 13.06.2015 10:43:35
 */
package de.freese.spring.ldap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.ldap.server.ApacheDSContainer;

/**
 * @author Thomas Freese
 */
@Configuration
public class Config
{
    /**
     * Erstellt ein neues Object.
     */
    public Config()
    {
        super();
    }

    /**
     * @return {@link ApacheDSContainer}
     * @throws Exception Falls was schief geht.
     */
    @Bean(name = "ldap-server", destroyMethod = "destroy")
    public ApacheDSContainer getLdapServer() throws Exception
    {
        ApacheDSContainer container = new ApacheDSContainer("dc=freese,dc=de", "file:/home/tommy/dokumente/linux/ldap/layout.ldif");
        container.setPort(3389);

        return container;
    }
}
