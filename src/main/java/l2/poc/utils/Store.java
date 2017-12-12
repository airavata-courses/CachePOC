package l2.poc.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import l2.poc.cache.Cache;
import l2.poc.cache.CacheFactory;


public class Store {
	private static String KEY_PREFIX="Cache";
	private AtomicInteger counter=new AtomicInteger(0);
	private static final Store STORE=new Store();
	private Map<String, Data> dataStore;
	private Cache<Data> cache=CacheFactory.getCacheFactory().getCache();
	
	public Store() {
		this.dataStore=new ConcurrentHashMap<>();
	}
	public static Store getStore() {
		return STORE;	
	}
	
	public String addData(Data data,boolean writeToCache) {
		String key=KEY_PREFIX+counter.getAndIncrement();
		this.dataStore.put(key, data);
		if(writeToCache) {
			cache.write(key, data);
		}
		return key;
	}
	
	
	
	public Data getData(String key) throws InterruptedException {
		Data value=cache.read(key);
		if(value!=null) {
			return value;
		}
		Thread.sleep(2000);
		return dataStore.get(key);
	}
	
	public void deleteData(String key) {
		cache.delete(key);
		this.dataStore.remove(key);
	}
	
	public  Map<String, Data> getAllData() throws InterruptedException {
		Map<String, Data> value=cache.readAll();
		if(value!=null) {
			return value;
		}
		Thread.sleep(2000);
		return this.dataStore;
	}
	
	public Cache<Data> getCache(){
		return this.cache;
	}

}
