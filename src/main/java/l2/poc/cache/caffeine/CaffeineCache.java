package l2.poc.cache.caffeine;

import java.util.Map;
import java.util.Optional;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import l2.poc.cache.Cache;

public class CaffeineCache<K, V> implements Cache<K, V> {
	private LoadingCache<K, V> loadingCache;
	private CacheServiceDiscovery cacheServiceDiscovery = CacheServiceDiscovery.getServiceDiscovery();

	@Override
	public void write(K key, V value) {
		loadingCache = Caffeine.newBuilder().writer(new CaffeineCacheWriter(cacheServiceDiscovery))
				.build(new CaffeineCacheLoader(cacheServiceDiscovery));
	}

	@Override
	public void delete(K key) {
		loadingCache.invalidate(key);
	}

	@Override
	public Map<K, V> readAll() {
		return loadingCache.asMap();
	}

	@Override
	public void clear() {
		loadingCache.cleanUp();

	}

	@Override
	public Optional<V> read(K key) {
		return Optional.ofNullable(loadingCache.get(key));
	}

	@Override
	public void close() {
		loadingCache.cleanUp();
	}

}
