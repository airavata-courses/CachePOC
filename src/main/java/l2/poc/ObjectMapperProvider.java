package l2.poc;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

	private final ObjectMapper objectMapper;

	public ObjectMapperProvider() {
		objectMapper = new ObjectMapper();
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}

}