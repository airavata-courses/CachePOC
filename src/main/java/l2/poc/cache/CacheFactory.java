package l2.poc.cache;

import l2.poc.cache.caffeine.CaffeineCache;
import l2.poc.cache.redis.RedisCache;
import l2.poc.utils.Data;

public class CacheFactory {
	public static final String CAFFEINE_CACHE = "caffeine";
	public static final String REDIS_CACHE = "redis";
	
	private static final CacheFactory CACHE_FACTORY=new CacheFactory();
	
	private String cacheType=null;

	public static CacheFactory getCacheFactory() {
		return CACHE_FACTORY;
	}
	
	public Cache<Data> getCache(){
		switch (cacheType) {
		case CAFFEINE_CACHE:
			return CaffeineCache.getCaffeineCache();
		case REDIS_CACHE:
			return RedisCache.getRedisCache();
		default:
			return new DefaultCache<>();
		}
	}

	public String getCacheType() {
		return cacheType;
	}

	public void setCacheType(String cacheType) {
		this.cacheType = cacheType;
	}
	
	
	
}
