package l2.poc.cache.caffeine;

import java.util.Map;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import l2.poc.cache.Cache;
import l2.poc.utils.Data;


public class CaffeineCache implements Cache <Data>{
	private LoadingCache<String, Data> loadingCache;
	private static final CaffeineCache CAFFEINE_CACHE=new CaffeineCache();
	private CacheServiceDiscovery cacheServiceDiscovery=CacheServiceDiscovery.getServiceDiscovery();

	public CaffeineCache() {
		loadingCache=Caffeine.newBuilder().writer(new CaffeineCacheWriter(cacheServiceDiscovery)).build(new CaffeineCacheLoader(cacheServiceDiscovery));
	}
	
	public static CaffeineCache getCaffeineCache() {
		return CAFFEINE_CACHE;
	}
	
	public Map<String, Data> readAll(){
		return loadingCache.asMap();
	}

	@Override
	public void write(String key, Data value) {
		loadingCache.put(key, value);
	}

	@Override
	public Data read(String key) {
		return loadingCache.get(key);
	}
	
	public void delete(String key) {
		loadingCache.invalidate(key);	
		}

}
