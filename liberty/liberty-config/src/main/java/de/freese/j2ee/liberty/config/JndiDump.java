// Created: 18.05.2018
package de.freese.j2ee.liberty.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Thomas Freese
 */
@Path("/")
public class JndiDump
{
    /**
     * @return {@link Map}
     *
     * @throws Exception Falls was schiefgeht.
     */
    @GET
    @Path("jndi")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getJndiTree() throws Exception
    {
        List<String> names = Arrays.asList("", "java:global", "java:app", "java:module", "java:comp");

        Map<String, Object> map = new TreeMap<>();
        InitialContext initialContext = new InitialContext();

        for (String name : names)
        {
            Context context = (Context) initialContext.lookup(name);
            map.put(name, dumpContextNameClassPair(context));
            // map.put(name, dumpContextBinding(context));
            context.close();
        }

        initialContext.close();

        return map;
    }

    /**
     * @param ctx {@link Context}
     *
     * @return {@link Map}
     *
     * @throws Exception Falls was schiefgeht.
     */
    protected Map<String, Object> dumpContextBinding(final Context ctx) throws Exception
    {
        NamingEnumeration<Binding> enumeration = ctx.listBindings("");
        Map<String, Object> map = new TreeMap<>();

        while (enumeration.hasMoreElements())
        {
            Binding binding = enumeration.next();
            String name = binding.getName();

            Object tmp = null;

            try
            {
                tmp = binding.getObject();
            }
            catch (Exception ex)
            {
                tmp = binding.getClassName();
            }

            if (tmp instanceof Context)
            {
                Context subContext = (Context) tmp;

                map.put(name, dumpContextBinding(subContext));
            }
            else
            {
                map.put(name, tmp.toString());
            }
        }

        return map;
    }

    /**
     * @param ctx {@link Context}
     *
     * @return {@link Map}
     *
     * @throws Exception Falls was schiefgeht.
     */
    protected Map<String, Object> dumpContextNameClassPair(final Context ctx) throws Exception
    {
        NamingEnumeration<NameClassPair> enumeration = ctx.list("");
        Map<String, Object> map = new TreeMap<>();

        while (enumeration.hasMoreElements())
        {
            NameClassPair pair = enumeration.next();
            String name = pair.getName();
            String className = pair.getClassName();

            boolean isSubContext = false;

            try
            {
                isSubContext = Class.forName(className).isAssignableFrom(Context.class);
            }
            catch (Exception ex)
            {
                // Ignore
            }

            if (isSubContext)
            {
                Context subContext = (Context) ctx.lookup(name);

                map.put(name, dumpContextNameClassPair(subContext));
            }
            else
            {
                try
                {
                    map.put(name, ctx.lookup(name).toString());
                }
                catch (Exception ex)
                {
                    map.put(name, className);
                }
            }
        }

        return map;
    }
}
