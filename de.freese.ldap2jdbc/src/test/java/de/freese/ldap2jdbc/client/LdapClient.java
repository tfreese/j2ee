/**
 * Created: 09.01.2015
 */
package de.freese.ldap2jdbc.client;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Thomas Freese
 */
public class LdapClient
{
    /**
     * Liefert nur den Wert des Keys.
     *
     * @param searchResult {@link SearchResult}
     * @param key String
     * @return String
     * @throws Exception Falls was schief geht.
     */
    private static String getValue(final SearchResult searchResult, final String key) throws Exception
    {
        Attribute attribute = searchResult.getAttributes().get(key);

        if (attribute == null)
        {
            return null;
        }

        return attribute.get().toString();
    }

    /**
     * @param args String[]
     * @throws Exception Falls was schief geht.
     */
    public static void main(final String[] args) throws Exception
    {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389");
        env.put(Context.SECURITY_PRINCIPAL, "cn=ldap,ou=users,dc=freese,dc=de");
        env.put(Context.SECURITY_CREDENTIALS, "ldapuser");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        // env.put(Context.SECURITY_AUTHENTICATION, "none");
        // env.put(Context.SECURITY_PROTOCOL, "ssl");

        InitialDirContext context = new InitialDirContext(env);

        // Specify the attributes to return
        String returnedAtts[] =
        {
            "*"
        };

        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        searchCtls.setReturningAttributes(returnedAtts);

        // specify the LDAP search filter, just users
        // String searchFilter = "mail=*THO*";
        String searchFilter = "(objectclass=*)";

        NamingEnumeration<SearchResult> search = context.search("ou=addressbook,dc=freese,dc=de", searchFilter, searchCtls);
        List<SearchResult> results = new ArrayList<>();

        while (search.hasMoreElements())
        {
            results.add(search.nextElement());
        }

        search.close();

        Collections.sort(results, new SearchResultComparator());

        for (SearchResult searchResult : results)
        {
            System.out.println(searchResult.toString());
        }

        context.close();
    }

    /**
     * @param pw {@link PrintWriter}
     * @param searchResult {@link SearchResult}
     * @param key String
     * @throws Exception Falls was schief geht.
     */
    private static void writeMultiValue(final PrintWriter pw, final SearchResult searchResult, final String key) throws Exception
    {
        Attribute attribute = searchResult.getAttributes().get(key);

        if (attribute == null)
        {
            return;
        }

        for (int i = 0; i < attribute.size(); i++)
        {
            String value = StringUtils.trim(attribute.get(i).toString());

            if (StringUtils.isBlank(value))
            {
                continue;
            }

            pw.printf("%s: %s%n", key, value);
        }
    }

    /**
     * @param pw {@link PrintWriter}
     * @param searchResult {@link SearchResult}
     * @param key String
     * @throws Exception Falls was schief geht.
     */
    private static void writeSingleValue(final PrintWriter pw, final SearchResult searchResult, final String key) throws Exception
    {
        Attribute attribute = searchResult.getAttributes().get(key);

        if (attribute == null)
        {
            return;
        }

        String value = StringUtils.trim(attribute.get().toString());

        if (StringUtils.isBlank(value))
        {
            return;
        }

        pw.printf("%s: %s%n", key, value);

    }

    /**
     * Erstellt ein neues {@link LdapClient} Object.
     */
    public LdapClient()
    {
        super();
    }
}
