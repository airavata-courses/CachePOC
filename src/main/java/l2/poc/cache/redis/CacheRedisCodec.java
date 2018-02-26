package l2.poc.cache.redis;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.lettuce.core.codec.RedisCodec;

public class CacheRedisCodec<K, V> implements RedisCodec<K, V> {
	private final ObjectMapper objectMapper = new ObjectMapper();

	private Class<K> keyClass;
	private Class<V> valueClass;

	public CacheRedisCodec(Class<K> kClass, Class<V> vCass) {
		this.keyClass = kClass;
		this.valueClass = vCass;
	}

	@Override
	public K decodeKey(ByteBuffer bytes) {
		return convertByteBufferToObject(bytes, this.keyClass);
	}

	@Override
	public V decodeValue(ByteBuffer bytes) {
		return convertByteBufferToObject(bytes, this.valueClass);
	}

	@Override
	public ByteBuffer encodeKey(K key) {
		return convertObjectToByteBufer(key);
	}

	@Override
	public ByteBuffer encodeValue(V value) {
		return convertObjectToByteBufer(value);
	}

	public ByteBuffer convertObjectToByteBufer(Object obj) {
		try {
			return ByteBuffer.wrap(objectMapper.writeValueAsString(obj).getBytes());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T convertByteBufferToObject(ByteBuffer byteBuffer, Class<T> cls) {
		try {
			int remaining = byteBuffer.remaining();
			if (remaining == 0) {
				return null;
			}
			byte[] temp = new byte[remaining];
			byteBuffer.get(temp);
			return objectMapper.readValue(new String(temp), cls);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
