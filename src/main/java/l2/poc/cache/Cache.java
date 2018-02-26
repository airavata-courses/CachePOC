package l2.poc.cache;

import java.util.Map;
import java.util.Optional;

public interface Cache<K, V> {

	public void write(K key, V value);

	public Optional<V> read(K key);

	public void delete(K key);

	public Map<K, V> readAll();

	public void clear();

	public void close();

}
