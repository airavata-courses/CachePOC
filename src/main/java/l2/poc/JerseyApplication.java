package l2.poc;

import java.util.Set;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class JerseyApplication extends ResourceConfig {

	public JerseyApplication() {
		register(new ObjectMapperProvider());
		register(new JacksonFeature());
		register(new MultiPartFeature());
		register(new DependencyInjectionBinder());
		System.out.println("Jersey Application Starting...................................................................................................................");
	}

	public JerseyApplication(Set<Class<?>> classes) {
		super(classes);
	}

	public JerseyApplication(Class<?>... classes) {
		super(classes);
	}

	public JerseyApplication(ResourceConfig original) {
		super(original);
	}
	
	

	@Override
	public ResourceConfig register(Class<?> componentClass) {
		return super.register(componentClass);
	}

}
