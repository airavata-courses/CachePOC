package l2.poc.cache.redis;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import l2.poc.cache.Cache;
import l2.poc.utils.Data;

public class RedisCache implements Cache<Data> {
	
	private static final RedisCache redisCache=new RedisCache();
	
	private RedisClient redisClient;

	@Override
	public void write(String key, Data value) {
		doAction((StatefulRedisConnection<String, Data> connection)->connection.async().set(key, value));
	}

	public void doAction(Consumer<StatefulRedisConnection<String, Data>> action) {
		StatefulRedisConnection<String, Data> connection=redisClient.connect(new CacheRedisCodec());
		action.accept(connection);
	}
	
	public <R> R doAction(Function<StatefulRedisConnection<String, Data>, R> action) {
		StatefulRedisConnection<String, Data> connection=redisClient.connect(new CacheRedisCodec());
		return action.apply(connection);
	}
	
	@Override
	public Data read(String key) {
		return doAction((StatefulRedisConnection<String, Data> connection)->connection.sync().get(key));
	}

	@Override
	public void delete(String key) {
		doAction((StatefulRedisConnection<String, Data> connection)->connection.async().del(key));
	}

	@Override
	public Map<String, Data> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public static RedisCache getRedisCache() {
		return redisCache;
	}

	public RedisClient getRedisClient() {
		return redisClient;
	}

	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}

}
