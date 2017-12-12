package l2.poc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import l2.poc.cache.CacheFactory;
import l2.poc.cache.caffeine.CacheServiceDiscovery;
import l2.poc.cache.redis.RedisCache;
import l2.poc.service.discovery.InstanceDetails;
import l2.poc.service.discovery.ServiceRegistration;
import l2.poc.service.discovery.ServiceRegistrationException;

@WebListener
public class WebApplicationInitializer implements ServletContextListener {

	private static final String REDIS_PORT = "redis.port";
	private static final String REDIS_HOST = "redis.host";
	private static final String APPLICATION_PATH = "path";
	private static final String APPLICATION_HOST = "host";
	private static final String APPLICATION_PORT = "port";
	private static final String ZOOKEPER_SLEEPS_RETRIES = "zookeper.sleeps.retries";
	private static final String ZOOKEPER_RETRY_TIMES = "zookeper.retry.times";
	private static final String ZOOKEEPER_ADDRESS = "zookeeper.address";
	private static final String SERVICE_NAME = "cache_poc";

	private CuratorFramework curatorFramework;
	private ServiceDiscovery<InstanceDetails> serviceDiscovery;
	private ServiceRegistration serviceRegistration = null;
	private RedisClient redisClient=null;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		String configFolderName = (String) servletContextEvent.getServletContext()
				.getInitParameter("configuration_folder");
		File propFile = new File(configFolderName + "/service.properties");
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(propFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		String cacheType = props.getProperty("cacheType", CacheFactory.CAFFEINE_CACHE);
		CacheFactory.getCacheFactory().setCacheType(cacheType);
		switch (cacheType) {
		case CacheFactory.CAFFEINE_CACHE:
			int port = Integer.parseInt(props.getProperty(APPLICATION_PORT));
			String address = props.getProperty(APPLICATION_HOST);
			String path = props.getProperty(APPLICATION_PATH);
			String zookeeperAddress = props.getProperty(ZOOKEEPER_ADDRESS);
			int zookeeperRetryTimes = Integer.parseInt(props.getProperty(ZOOKEPER_RETRY_TIMES));
			int zookeeperSleepRetryTime = Integer.parseInt(props.getProperty(ZOOKEPER_SLEEPS_RETRIES));
			curatorFramework = CuratorFrameworkFactory.newClient(zookeeperAddress,
					new RetryNTimes(zookeeperRetryTimes, zookeeperSleepRetryTime));
			curatorFramework.start();
			serviceRegistration = new ServiceRegistration(curatorFramework, address, port, SERVICE_NAME, path,
					new InstanceDetails(100));
			try {
				serviceRegistration.registerService();
				serviceDiscovery = serviceRegistration.getServiceDiscovery();
				CacheServiceDiscovery.getServiceDiscovery().setServiceDiscovery(serviceDiscovery)
						.setId(serviceRegistration.getInstanceId());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			break;
		case CacheFactory.REDIS_CACHE:
			redisClient = RedisClient.create(RedisURI.create(props.getProperty(REDIS_HOST), Integer.parseInt(props.getProperty(REDIS_PORT))));
			RedisCache.getRedisCache().setRedisClient(redisClient);
			break;
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		System.out.println("Shutting down!");
		if (serviceRegistration != null) {
			try {
				serviceRegistration.unregisterService();
			} catch (ServiceRegistrationException e) {
				throw new RuntimeException(e);
			}
		}
		if (serviceDiscovery != null) {
			try {
				serviceDiscovery.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (curatorFramework != null) {
			curatorFramework.close();
		}
		if(redisClient!=null) {
			redisClient.shutdown();
		}

	}

}