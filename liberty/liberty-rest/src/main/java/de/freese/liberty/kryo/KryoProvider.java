// Created: 14 März 2025
package de.freese.liberty.kryo;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.util.MapReferenceResolver;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * @author Thomas Freese
 */
// @Provider
public final class KryoProvider {
    private static final Map<String, Kryo> CACHE = new HashMap<>();

    // @Produces
    // @RequestScoped
    public Kryo getKryo() {
        return CACHE.computeIfAbsent(Thread.currentThread().getName(), key -> {
            final Kryo kryo = new Kryo();
            kryo.setClassLoader(Thread.currentThread().getContextClassLoader());
            kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
            kryo.setReferences(true); // Avoid Recursion.
            kryo.setOptimizedGenerics(false);
            kryo.setReferenceResolver(new MapReferenceResolver() {
                @Override
                public boolean useReferences(final Class type) {
                    return super.useReferences(type) && !String.class.equals(type); // For Problems with String References.
                }
            });

            kryo.setRegistrationRequired(false);
            kryo.setWarnUnregisteredClasses(false);

            return kryo;
        });
    }
}
