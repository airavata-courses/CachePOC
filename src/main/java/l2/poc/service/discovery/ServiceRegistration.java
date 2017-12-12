package l2.poc.service.discovery;

import java.util.Collection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

/**
 * 
 * @author Eldho Mathulla
 *
 */
public class ServiceRegistration {

	private CuratorFramework curatorFramework;
	private String address;
	private int port;
	private InstanceDetails instanceDetails;
	private String serviceName;
	private ServiceInstance<InstanceDetails> serviceInstance;
	private ServiceDiscovery<InstanceDetails> serviceDiscovery;
	private String path;

	public ServiceRegistration(CuratorFramework curatorFramework, String address, int port, String serviceName, String path, InstanceDetails instanceDetails) {
		this.curatorFramework = curatorFramework;
		this.address = address;
		this.port = port;
		this.instanceDetails = instanceDetails;
		this.serviceName = serviceName;
		this.path = path;
	}

	public void registerService() throws ServiceRegistrationException {
		try {
			ServiceInstanceBuilder<InstanceDetails> serviceInstanceBuilder = ServiceInstance.builder();
			serviceInstance = serviceInstanceBuilder.address(address).port(port).name(serviceName).uriSpec(new UriSpec("{scheme}://{address}:{port}" + path)).payload(instanceDetails).build();
			ServiceDiscoveryBuilder<InstanceDetails> serviceDiscoveryBuilder = ServiceDiscoveryBuilder.builder(InstanceDetails.class);
			JsonInstanceSerializer<InstanceDetails> serializer = new JsonInstanceSerializer<InstanceDetails>(InstanceDetails.class);
			serviceDiscovery = serviceDiscoveryBuilder.basePath("services").client(curatorFramework).serializer(serializer).thisInstance(serviceInstance).build();
			serviceDiscovery.start();
			hasRegistered();
		} catch (Exception e) {
			throw new ServiceRegistrationException(e);
		}
	}

	private void hasRegistered() throws ServiceRegistrationException {
		long count = 0;
		try {
			ServiceProvider<InstanceDetails> serviceProvider = serviceDiscovery.serviceProviderBuilder().serviceName(serviceName).build();
			serviceProvider.start();
			Collection<ServiceInstance<InstanceDetails>> serviceInstances = serviceProvider.getAllInstances();
			count = serviceInstances.stream().map(ServiceInstance::getId).filter((String id) -> id.equals(serviceInstance.getId())).count();
		} catch (Exception e) {
			throw new ServiceException("Service Registration failed: " + this.serviceName, e);
		}
		System.out.println(count);
		if (count == 0) {
			throw new ServiceRegistrationException("Service Registration failed");
		}
	}

	public void unregisterService() throws ServiceRegistrationException {
		try {
			serviceDiscovery.unregisterService(serviceInstance);
		} catch (Exception e) {
			throw new ServiceRegistrationException(e);
		}
	}

	public void updateWorkLoad(int workLoad) throws ServiceUpdationException {
		try {
			instanceDetails.setWorkLoad(instanceDetails.getWorkLoad() + workLoad);
			serviceDiscovery.updateService(serviceInstance);
		} catch (Exception e) {
			throw new ServiceUpdationException(e);
		}
	}
	
	public String getInstanceId() {
		return serviceInstance.getId();
	}
	
	public ServiceDiscovery<InstanceDetails> getServiceDiscovery(){
		return serviceDiscovery;
	}
	
	

}