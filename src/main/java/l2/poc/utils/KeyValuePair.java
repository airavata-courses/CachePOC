package l2.poc.utils;

public class KeyValuePair<T> {
	private String key;
	private T value;
	
	public KeyValuePair(String key,T value) {
		this.setKey(key);
		this.setValue(value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
