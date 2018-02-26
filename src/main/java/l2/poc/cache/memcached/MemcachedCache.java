package l2.poc.cache.memcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import l2.poc.cache.Cache;
import l2.poc.cache.CacheConfiguration;
import l2.poc.cache.CacheException;
import l2.poc.cache.NotImplementedException;
import net.spy.memcached.MemcachedClient;

public class MemcachedCache<K, V> implements Cache<K, V> {

	private static final String MEMCACHED_EXPIRATION_KEY = "memcached.expiration";
	private static final String MEMCACHED_ADDRESSES_KEY = "memcached.addresses";
	private MemcachedClient memcachedClient;
	private int expiration = 60;

	public MemcachedCache(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public MemcachedCache(MemcachedClient memcachedClient, int expiration) {
		this(memcachedClient);
		this.setExpiration(expiration);
	}

	public MemcachedCache(CacheConfiguration<?, ?> cacheConfiguration) {
		List<String> addresses = cacheConfiguration.getConfigurations(MEMCACHED_ADDRESSES_KEY);
		List<InetSocketAddress> inetSocketAddresses = addresses.stream().map((String address) -> {
			String[] hostPort = address.split(":");
			return new InetSocketAddress(hostPort[0], Integer.parseInt(hostPort[1]));
		}).collect(Collectors.toList());
		try {
			this.memcachedClient = new MemcachedClient(inetSocketAddresses);
		} catch (IOException e) {
			throw new CacheException("Initializing Memcache failed", e);
		}
		cacheConfiguration.getOptionalConfiguration(MEMCACHED_EXPIRATION_KEY)
				.ifPresent((Object expiration) -> this.expiration = (Integer) expiration);
		this.expiration = (int) cacheConfiguration.getConfiguration(MEMCACHED_EXPIRATION_KEY);
	}

	@Override
	public void write(K key, V value) {
		memcachedClient.add(key.toString(), this.expiration, value);
	}

	@Override
	public Optional<V> read(K key) {
		@SuppressWarnings("unchecked")
		V v = (V) memcachedClient.get(key.toString());
		return Optional.ofNullable(v);
	}

	@Override
	public void delete(K key) {
		memcachedClient.delete(key.toString());
	}

	@Override
	public Map<K, V> readAll() {
		throw new NotImplementedException("Read All function was not implemented");
	}

	@Override
	public void clear() {
		this.memcachedClient.flush();
	}

	public int getExpiration() {
		return expiration;
	}

	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}

	@Override
	public void close() {
		this.memcachedClient.shutdown();
	}

}
