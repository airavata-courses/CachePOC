package l2.poc.cache;

import java.util.Map;
import java.util.Optional;

public class DefaultCache<K, V> implements Cache<K, V> {

	@Override
	public void write(K key, V value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<V> read(K key) {
		// TODO Auto-generated method stub
		return Optional.empty();
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
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
