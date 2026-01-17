// Created: 17 Jan. 2026
package de.freese.liberty.cache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.annotation.CacheResult;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Singleton
@Startup
public class MyCachableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyCachableBean.class);

    @Inject
    private CacheManager cacheManager;

    @Inject
    private CacheProducer cacheProducer;

    @CacheResult(cacheName = "countryByCode")
    public String getCountry(final String code) {
        return code.substring(0, 1).toUpperCase() + code.substring(1);
    }

    @PostConstruct
    public void myPostConstruct() {
        LOGGER.info("myPostConstruct");

        final Cache<String, String> cache = cacheProducer.getCountryByCode();
        cache.put("a", "aa");
    }
}
