package l2.poc.cache.redis;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import javafx.scene.chart.PieChart.Data;
import l2.poc.cache.Cache;
import l2.poc.cache.CacheConfiguration;

public class RedisCache<K, V> implements Cache<K, V> {

	private static final String REDIS_PORT = null;
	private static final String REDIS_HOST = null;
	private RedisClient redisClient;
	private Class<K> keyClass = null;
	private Class<V> valueClass = null;

	public RedisCache(RedisClient redisClient) {
		// TODO Auto-generated constructor stub
	}

	public RedisCache(CacheConfiguration<K, V> cacheConfiguration) {
		this.redisClient = RedisClient.create(RedisURI.create((String) cacheConfiguration.getConfiguration(REDIS_HOST),
				(int) cacheConfiguration.getConfiguration(REDIS_PORT)));
		this.keyClass = cacheConfiguration.getKeyClass();
		this.valueClass = cacheConfiguration.getValueClass();
	}

	@Override
	public void write(K key, V value) {
		doAction((StatefulRedisConnection<K, V> connection) -> connection.async().set(key, value));
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
		this.redisClient.shutdown();
	}

	public void doAction(Consumer<StatefulRedisConnection<K, V>> action) {
		StatefulRedisConnection<K, V> connection = redisClient
				.connect(new CacheRedisCodec<>(this.keyClass, this.valueClass));
		action.accept(connection);
	}

	public <R> R doAction(Function<StatefulRedisConnection<String, Data>, R> action) {
		StatefulRedisConnection<String, Data> connection = redisClient
				.connect(new CacheRedisCodec<>(String.class, Data.class));
		return action.apply(connection);
	}

}
