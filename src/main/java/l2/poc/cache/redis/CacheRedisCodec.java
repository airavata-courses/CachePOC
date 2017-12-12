package l2.poc.cache.redis;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.lettuce.core.codec.RedisCodec;
import l2.poc.utils.Data;

public class CacheRedisCodec implements RedisCodec<String, Data> {
	private final ObjectMapper objectMapper=new ObjectMapper();

	@Override
	public String decodeKey(ByteBuffer bytes) {
		return convertByteBufferToObject(bytes, String.class);
	}

	@Override
	public Data decodeValue(ByteBuffer bytes) {
		return convertByteBufferToObject(bytes, Data.class);
	}

	@Override
	public ByteBuffer encodeKey(String key) {
		return convertObjectToByteBufer(key);
	}

	@Override
	public ByteBuffer encodeValue(Data value) {
		return convertObjectToByteBufer(value);
	}
	
	public ByteBuffer convertObjectToByteBufer(Object obj) {
		try {
			return ByteBuffer.wrap(objectMapper.writeValueAsString(obj).getBytes());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> T convertByteBufferToObject(ByteBuffer byteBuffer,Class<T> cls) {
		try {
			return objectMapper.readValue(new String(byteBuffer.array()), cls);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
