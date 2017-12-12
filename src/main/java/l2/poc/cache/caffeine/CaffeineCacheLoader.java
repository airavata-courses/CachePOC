package l2.poc.cache.caffeine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import javax.cache.integration.CacheLoaderException;
import javax.ws.rs.client.WebTarget;

import com.github.benmanes.caffeine.cache.CacheLoader;

import l2.poc.utils.Data;

public class CaffeineCacheLoader implements CacheLoader<String, Data> {
	private static final Logger LOGGER=Logger.getGlobal();
	private CacheServiceDiscovery cacheServiceDiscovery;
	
	public CaffeineCacheLoader(CacheServiceDiscovery cacheServiceDiscovery) {
		this.cacheServiceDiscovery=cacheServiceDiscovery;
	}

	 public Data load(String s) {
	        return null;
	    }

	    public Map<String, Data> loadAll(Iterable<? extends String> iterable) throws CacheLoaderException {
	    	LOGGER.info("loading cache....");
	    	try {
				return cacheServiceDiscovery.getCacheInstance().map((WebTarget target)->target.path("rest/cache/read/all").request().get(Map.class)
				).orElse(new HashMap<>());
			} catch (Exception e) {
				throw new CacheLoaderException(e);
			}
	    }

}
