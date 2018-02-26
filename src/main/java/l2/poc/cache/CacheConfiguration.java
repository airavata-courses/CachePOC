package l2.poc.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CacheConfiguration<K, V> {

	private static final String CACHE_TYPE_KEY = "cache.type";
	public static final String DEFAULT_CACHE = "default";
	public static final String REDIS_CACHE = "redis";
	public static final String CAFFEINE_CACHE_REPLICATION = "cache_replication";
	public static final String MEMCACHED = "memcached";

	private String cacheType = DEFAULT_CACHE;
	private Map<String, Object> configurations = new HashMap<>();
	private Class<K> keyClass = null;
	private Class<V> valueClass = null;

	public CacheConfiguration(String cacheType) {
		this.setCacheType(cacheType);
	}

	public CacheConfiguration(File configurationFile, Class<K> keyClass, Class<V> valueClass) {
		Properties configurationProperties = new Properties();
		try {
			configurationProperties.load(new BufferedReader(new FileReader(configurationFile)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.cacheType = configurationProperties.getProperty(CACHE_TYPE_KEY);
		configurationProperties.remove(CACHE_TYPE_KEY);
		this.configurations = configurationProperties.entrySet().stream().map((Entry<Object, Object> entry) -> {
			entry.setValue(parseData((String) entry.getValue()));
			return entry;
		}).collect(
				Collector.of(
						HashMap::new, (Map<String, Object> map, Entry<Object, Object> entry) -> map
								.put((String) entry.getKey(), entry.getValue()),
						(Map<String, Object> map1, Map<String, Object> map2) -> {
							map1.putAll(map2);
							return map1;
						}));
		System.out.println(this.configurations);
		this.setKeyClass(keyClass);
		this.setValueClass(valueClass);
	}

	public void setConfuguration(String key, Object value) {
		configurations.put(key, value);
	}

	public Object getConfiguration(String key) {
		return configurations.get(key);
	}

	@SuppressWarnings("unchecked")
	public <R> Optional<R> getOptionalConfiguration(String key) {
		return Optional.ofNullable((R) this.getConfiguration(key));
	}

	public boolean hasConfiguration(String key) {
		return configurations.containsKey(key);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getConfigurations(String key) {
		String config = (String) this.getConfiguration(key);
		return Arrays.stream(config.split(",")).map((String s) -> (T) parseData(s)).collect(Collectors.toList());
	}

	public String getCacheType() {
		return cacheType;
	}

	private void setCacheType(String cacheType) {
		this.cacheType = cacheType;
	}

	private Object parseData(String data) {
		if (data.matches("[0-9]+")) {
			return Integer.parseInt(data);
		} else if (data.matches("true|false")) {
			return Boolean.parseBoolean(data);
		}
		return data;
	}

	public Class<V> getValueClass() {
		return valueClass;
	}

	public void setValueClass(Class<V> valueClass) {
		this.valueClass = valueClass;
	}

	public Class<K> getKeyClass() {
		return keyClass;
	}

	public void setKeyClass(Class<K> keyClass) {
		this.keyClass = keyClass;
	}

}
