package l2.poc;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class JerseyApplication extends ResourceConfig {

	public JerseyApplication(@Context ServletContext servletContext) {
		packages(true, "l2.poc");
		register(new ObjectMapperProvider());
		register(new JacksonFeature());
		register(new MultiPartFeature());
		register(ReponseFilter.class);
		String configFolder = servletContext.getInitParameter("configuration_folder");
		register(new DependencyInjectionBinder(configFolder));
		System.out.println(
				"Jersey Application Starting...................................................................................................................");
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
