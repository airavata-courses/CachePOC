package l2.poc.cache.caffeine;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.sse.SseFeature;

import l2.poc.service.discovery.InstanceDetails;

public class CacheServiceDiscovery {
	private static final String serviceName = "cache_poc";
	private static CacheServiceDiscovery SERVICE_DISCOVERY=new CacheServiceDiscovery();
	private ServiceDiscovery<InstanceDetails> serviceDiscovery;
	private Client client;
	private String id;


	
	private CacheServiceDiscovery() {
		client = ClientBuilder.newBuilder().register(new MultiPartFeature()).register(new SseFeature()).build();
	}
	
	public static CacheServiceDiscovery getServiceDiscovery() {
		return SERVICE_DISCOVERY;
	}
	
	public Optional<WebTarget> getCacheInstance() throws Exception {
		ServiceProvider<InstanceDetails> serviceProvider = createServiceProvider();
		ServiceInstance<InstanceDetails> serviceInstance=null;
		Collection<ServiceInstance<InstanceDetails>> serviceInstances=serviceProvider.getAllInstances();
		for( ServiceInstance<InstanceDetails> inst:serviceInstances) {
			if(!inst.getId().equals(id)) {
				serviceInstance=inst;
				break;
			}
		}
		return Optional.ofNullable(serviceInstance).map(createServiceInstalnceToURIConverter());
	}

	private Function<ServiceInstance<InstanceDetails>, ? extends WebTarget> createServiceInstalnceToURIConverter() {
		return (ServiceInstance<InstanceDetails> instance)->client.target(instance.buildUriSpec());
	}

	private ServiceProvider<InstanceDetails> createServiceProvider() throws Exception {
		serviceDiscovery.start();
		ServiceProvider<InstanceDetails> serviceProvider = serviceDiscovery.serviceProviderBuilder().serviceName(serviceName).build();
		serviceProvider.start();
		return serviceProvider;
	}
	
	public List<WebTarget> getCacheInstances() throws Exception{
		ServiceProvider<InstanceDetails> serviceProvider = createServiceProvider();
		return serviceProvider.getAllInstances().parallelStream().map(createServiceInstalnceToURIConverter()).collect(Collectors.toList());
	}

	public CacheServiceDiscovery setServiceDiscovery(ServiceDiscovery<InstanceDetails> serviceDiscovery) {
		this.serviceDiscovery = serviceDiscovery;
		return this;
	}
	
	public CacheServiceDiscovery setId(String id) {
		this.id=id;
		return this;
	}

}
