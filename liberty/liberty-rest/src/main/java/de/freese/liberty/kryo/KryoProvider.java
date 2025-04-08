// Created: 14 März 2025
package de.freese.liberty.kryo;

import java.time.Duration;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Produces;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.SerializerFactory;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.util.MapReferenceResolver;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
// @Provider
@Produces
// @MyKryo
@RequestScoped
@SuppressWarnings("rawtypes")
public class KryoProvider {
    private static final Cache<String, Kryo> CACHE = Caffeine.newBuilder().expireAfterWrite(Duration.ofHours(1L)).build();
    private static final Logger LOGGER = LoggerFactory.getLogger(KryoProvider.class);

    public Kryo getKryo() {
        LOGGER.info("obtain instance");

        return CACHE.get(Thread.currentThread().getName(), key -> {
            LOGGER.info("create instance for: {}", key);

            final Kryo kryo = new Kryo();
            kryo.setClassLoader(Thread.currentThread().getContextClassLoader());
            kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
            kryo.setReferences(true); // Avoid Recursion.
            kryo.setCopyReferences(true); // Avoid Recursion.
            kryo.setOptimizedGenerics(false);
            kryo.setReferenceResolver(new MapReferenceResolver() {
                @Override
                public boolean useReferences(final Class type) {
                    return super.useReferences(type) && !String.class.equals(type); // For Problems with String References.
                }
            });

            kryo.setRegistrationRequired(false);
            kryo.setWarnUnregisteredClasses(false);

            // Supports different JRE Versions and different order of fields.
            final SerializerFactory.CompatibleFieldSerializerFactory serializerFactory = new SerializerFactory.CompatibleFieldSerializerFactory();
            serializerFactory.getConfig().setExtendedFieldNames(true);
            serializerFactory.getConfig().setFieldsAsAccessible(true);
            serializerFactory.getConfig().setReadUnknownFieldData(true);
            // serializerFactory.getConfig().setChunkedEncoding(true);
            kryo.setDefaultSerializer(serializerFactory);

            // final Set<Class<?>> registrationClasses = Set.of(
            //         // java.lang.Object.class,
            //         // java.lang.String.class,
            //         // java.lang.Long.class,
            //         java.util.List.class,
            //         // java.util.List.of(1).getClass(), // java.util.ImmutableCollections.List12
            //         java.util.List.of(1, 2, 3, 4).getClass() // java.util.ImmutableCollections.ListN
            // );
            //
            // // registrationClasses.forEach(clazz -> kryo.register(clazz, kryo.getNextRegistrationId()));
            // registrationClasses.forEach(kryo::register);

            return kryo;
        });
    }
}
