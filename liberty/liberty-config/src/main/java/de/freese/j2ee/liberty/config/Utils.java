// Created:04.06.2018
package de.freese.j2ee.liberty.config;

import java.time.Duration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/***
 * @author Thomas Freese
 */
@SuppressWarnings("unchecked")
public final class Utils {

    private static final Cache<String, Object> CACHE = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(15))
            .build();

    public static <T> T ejb(final Class<T> type) {
        final Object bean = CACHE.get(type.getName(), key -> lookupBean(type));

        return type.cast(bean);
    }

    public static <T> T inject(final Class<T> type) {
        final Object bean = CACHE.get(type.getName(), key -> {
            final BeanManager bm = CDI.current().getBeanManager();
            final Bean<T> b = (Bean<T>) bm.getBeans(type).iterator().next();

            return bm.getReference(b, type, bm.createCreationalContext(b));
        });

        return type.cast(bean);
    }

    public static <T> T lookup(final String jndiName, final Class<T> type) {
        Object object = null;

        try {
            final Context context = new InitialContext();
            object = context.lookup(jndiName);
            context.close();
        }
        catch (NamingException ex) {
            throw new RuntimeException(ex);
        }

        return type.cast(object);
    }

    private static <T> T lookupBean(final Class<T> type) {
        return lookup("java:module/" + type.getSimpleName(), type);
    }

    private Utils() {
        super();
    }
}
