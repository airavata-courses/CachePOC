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
		// TODO Auto-generated method stub
	}

	@Override
	public Map<K, V> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<V> read(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
