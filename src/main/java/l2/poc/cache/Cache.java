package l2.poc.cache;

import java.util.Map;



public interface  Cache<T> {

	public void write(String key,T value);
	
	public T read(String key);
	
	public void delete(String key);

	public Map<String, T> readAll();
	
}
