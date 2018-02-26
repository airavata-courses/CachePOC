package l2.poc.cache;

import l2.poc.cache.caffeine.CaffeineCache;
import l2.poc.cache.memcached.MemcachedCache;
import l2.poc.cache.redis.RedisCache;

public class CacheFactory {

	private static final CacheFactory CACHE_FACTORY = new CacheFactory();

	public static CacheFactory getCacheFactory() {
		return CACHE_FACTORY;
	}

	public <K, V> Cache<K, V> createCache(CacheConfiguration<K, V> cacheConfiguration) {
		switch (cacheConfiguration.getCacheType()) {
		case CacheConfiguration.REDIS_CACHE:
			return new RedisCache<>(cacheConfiguration);
		case CacheConfiguration.CAFFEINE_CACHE_REPLICATION:
			return new CaffeineCache<>();
		case CacheConfiguration.MEMCACHED:
			return new MemcachedCache<>(cacheConfiguration);
		case CacheConfiguration.DEFAULT_CACHE:
		default:
			return new DefaultCache<>();
		}
	}
}
