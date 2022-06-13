// Created:04.06.2018
package de.freese.j2ee.liberty.config;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/***
 * @author Thomas Freese
 */
public final class Utils
{
    /**
     *
     */
    private static final ThreadLocal<Map<String, Object>> CACHE = ThreadLocal.withInitial(HashMap::new);

    /**
     * @param type Class
     *
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public static <T> T ejb(final Class<T> type)
    {
        Object bean = CACHE.get().computeIfAbsent(type.getName(), key -> lookupBean(type));

        return (T) bean;
    }

    /**
     * @param type Class
     *
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public static <T> T inject(final Class<T> type)
    {
        Object bean = CACHE.get().computeIfAbsent(type.getName(), key ->
        {
            BeanManager bm = CDI.current().getBeanManager();
            Bean<T> b = (Bean<T>) bm.getBeans(type).iterator().next();
            
            return bm.getReference(b, type, bm.createCreationalContext(b));
        });

        return (T) bean;
    }

    /**
     * Führt einen JNDI-Lookup durch.
     *
     * @param jndiName String
     *
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public static <T> T lookup(final String jndiName)
    {
        Object object = null;

        try
        {
            Context context = new InitialContext();
            object = context.lookup(jndiName);
            context.close();
        }
        catch (NamingException nex)
        {
            throw new RuntimeException(nex);
        }

        return (T) object;
    }

    /**
     * Führt einen JNDI-Lookup durch.
     *
     * @param type Class
     *
     * @return Object
     */
    private static <T> T lookupBean(final Class<T> type)
    {
        return lookup("java:module/" + type.getSimpleName());
    }

    /**
     * Erstellt ein neues {@link Utils} Object.
     */
    private Utils()
    {
        super();
    }
}
