package l2.poc;

import javax.inject.Inject;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEvent.Type;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import com.mongodb.MongoClient;

import l2.poc.cache.Cache;
import l2.poc.utils.Data;

public class JerseyApplicationEventListener implements ApplicationEventListener {
	@Inject
	MongoClient mongoClient;

	@Inject
	Cache<Integer, Data> cache;

	@Override
	public void onEvent(ApplicationEvent event) {
		if (event.getType() == Type.DESTROY_FINISHED) {
			mongoClient.close();
			cache.close();
		}
	}

	@Override
	public RequestEventListener onRequest(RequestEvent requestEvent) {
		// TODO Auto-generated method stub
		return null;
	}

}
