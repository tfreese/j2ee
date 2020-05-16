/**
 * Created: 03.06.2018
 */

package de.freese.jpa;

import org.hibernate.cache.ehcache.internal.EhcacheRegionFactory;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.ehcache.Cache;

/**
 * Erzeugt On-The-Fly nicht vorhandene EhCaches, ohne eine Exception zu werfen.
 *
 * @author Thomas Freese
 * @deprecated Use ConfigSettings.MISSING_CACHE_STRATEGY instead
 */
@Deprecated
public class OnTheFlyEhcacheRegionFactory extends EhcacheRegionFactory
{
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OnTheFlyEhcacheRegionFactory.class);

    /**
     *
     */
    private static final long serialVersionUID = 5560790286422926761L;

    /**
     * Erstellt ein neues {@link OnTheFlyEhcacheRegionFactory} Object.
     */
    public OnTheFlyEhcacheRegionFactory()
    {
        super();
    }

    /**
     * Erstellt ein neues {@link OnTheFlyEhcacheRegionFactory} Object.
     *
     * @param cacheKeysFactory {@link CacheKeysFactory}
     */
    public OnTheFlyEhcacheRegionFactory(final CacheKeysFactory cacheKeysFactory)
    {
        super(cacheKeysFactory);
    }

    /**
     * @see org.hibernate.cache.ehcache.internal.EhcacheRegionFactory#createCache(java.lang.String)
     */
    @Override
    protected Cache createCache(final String regionName)
    {
        LOGGER.info("create cache: {}", regionName);
        getCacheManager().addCache(regionName);

        return getCacheManager().getCache(regionName);
    }

}
