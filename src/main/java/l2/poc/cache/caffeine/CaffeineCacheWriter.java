package l2.poc.cache.caffeine;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.cache.integration.CacheWriterException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.RemovalCause;

import l2.poc.utils.KeyValuePair;

public class CaffeineCacheWriter<K, V> implements CacheWriter<K, V> {
	private static final Logger LOGGER = Logger.getGlobal();
	private CacheServiceDiscovery cacheServiceDiscovery;
	private Executor executor = Executors.newCachedThreadPool();

	public CaffeineCacheWriter(CacheServiceDiscovery cacheServiceDiscovery) {
		this.cacheServiceDiscovery = cacheServiceDiscovery;
	}

	@Override
	public void delete(K key, V value, RemovalCause arg2) {
		executor.execute(() -> {
			try {
				cacheServiceDiscovery.getCacheInstances().parallelStream().forEach((WebTarget webTarget) -> {
					webTarget.path("rest/delete").queryParam("key", key).request().delete();
				});
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "cache deletion failed", e);
			}
		});
	}

	@Override
	public void write(K arg0, V arg1) {
		executor.execute(() -> {
			try {
				cacheServiceDiscovery.getCacheInstances().parallelStream()
						.forEach((WebTarget target) -> target.path("rest/cache/write").request()
								.post(Entity.entity(new KeyValuePair<K, V>(arg0, arg1), MediaType.APPLICATION_JSON)));
			} catch (Exception e) {
				throw new CacheWriterException(e);
			}
		});
	}

}
