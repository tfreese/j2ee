/**
 * Created on 13.06.2015 10:43:35
 */
package de.freese.spring.ldap;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.directory.server.core.DefaultDirectoryService;
import org.apache.directory.server.core.entry.ServerEntry;
import org.apache.directory.server.core.partition.Partition;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmIndex;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.store.LdifFileLoader;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.apache.directory.server.xdbm.Index;
import org.apache.directory.shared.ldap.name.LdapDN;
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

    // /**
    // * @return {@link ApacheDSContainer}
    // * @throws Exception Falls was schief geht.
    // */
    // @Bean(name = "ldap-server", destroyMethod = "destroy")
    // public ApacheDSContainer getLdapServer() throws Exception
    // {
    // ApacheDSContainer container = new ApacheDSContainer("dc=freese,dc=de", "file:/tmp/addressbook.ldif");
    // container.setPort(3389);
    //
    // return container;
    // }

    /**
     * @return {@link ApacheDSContainer}
     * @throws Exception Falls was schief geht.
     */
    @Bean(name = "ldap-server", destroyMethod = "shutdown")
    public DefaultDirectoryService getLdapServer() throws Exception
    {
        // ApacheDSContainer container = new ApacheDSContainer("dc=freese,dc=de", "file:/tmp/addressbook.ldif");
        // container.setPort(3389);

        // Initialize the LDAP service
        DefaultDirectoryService service = new DefaultDirectoryService();

        File workingDir = new File("/tmp/apacheds");
        workingDir.delete();
        workingDir.mkdirs();
        service.setWorkingDirectory(workingDir);
        service.setShutdownHookEnabled(true);

        // Disable the ChangeLog system
        service.getChangeLog().setEnabled(false);

        // Create a new partition named 'apache'.
        Partition apachePartition = new JdbmPartition();
        apachePartition.setId("apache");
        apachePartition.setSuffix("dc=apache,dc=org");
        service.addPartition(apachePartition);

        // Index some attributes on the rootPartition
        Set<Index<?, ServerEntry>> indexedAttributes = new HashSet<>();

        for (String attribute : Arrays.asList("objectClass", "ou", "uid"))
        {
            indexedAttributes.add(new JdbmIndex<String, ServerEntry>(attribute));
        }

        ((JdbmPartition) apachePartition).setIndexedAttributes(indexedAttributes);

        // And start the service
        LdapServer server = new LdapServer();
        server.setDirectoryService(service);
        server.setTransports(new TcpTransport(3389));
        service.startup();
        server.start();

        // Inject the apache root entry if it does not already exist
        if (!service.getAdminSession().exists(apachePartition.getSuffixDn()))
        {
            LdapDN dnApache = new LdapDN("dc=Apache,dc=Org");
            ServerEntry entryApache = service.newEntry(dnApache);
            entryApache.add("objectClass", "top", "domain", "extensibleObject");
            entryApache.add("dc", "Apache");
            service.getAdminSession().add(entryApache);
        }

        // LDIF laden
        // Registries registries = service.getRegistries();

        Partition partition = new JdbmPartition();
        partition.setId("rootPartition");
        partition.setSuffix("dc=freese,dc=de");
        service.addPartition(partition);

        LdifFileLoader loader = new LdifFileLoader(service.getAdminSession(), new File("/tmp/addressbook.ldif"), null, getClass().getClassLoader());
        loader.execute();

        return service;
    }
}
