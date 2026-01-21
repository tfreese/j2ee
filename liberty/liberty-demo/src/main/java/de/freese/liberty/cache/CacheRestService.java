// Created: 17 Jan. 2026
package de.freese.liberty.cache;

import javax.cache.annotation.CacheResult;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@Path("cache")
public class CacheRestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheRestService.class);

    // @Inject
    // private javax.cache.CacheManager cacheManager;

    @Inject
    private CacheManager cacheManagerBean;

    // @Inject
    // @NamedCache("countryByCode")
    // private Cache<String, String> countryByCodeCache;

    @Path("country/{code}")
    @CacheResult(cacheName = "countryByCode")
    public String getCountry(@PathParam("code") final String code) {
        return code.substring(0, 1).toUpperCase() + code.substring(1);
    }

    @PostConstruct
    void afterStartup() {
        LOGGER.info("afterStartup");

        cacheManagerBean.getCountryByCodeCache().put("ab", "aB");
    }
}
