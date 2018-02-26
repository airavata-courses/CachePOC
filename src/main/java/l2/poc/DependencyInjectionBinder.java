package l2.poc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.ext.Provider;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.glassfish.jersey.internal.inject.AbstractBinder;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import l2.poc.cache.Cache;
import l2.poc.cache.CacheConfiguration;
import l2.poc.cache.CacheFactory;
import l2.poc.utils.Data;
import l2.poc.utils.Store;

@Provider
public class DependencyInjectionBinder extends AbstractBinder {

	private static final String CACHE_CONFIG_FILE = "cache_config.properties";
	private static final String MONGO_CONFIG_FILE = "mongo_db.properties";
	private static final Logger LOGGER = Logger.getLogger(DependencyInjectionBinder.class.getName());
	private String configurationFolder;

	public DependencyInjectionBinder(String configurationFolder) {
		this.configurationFolder = configurationFolder;
	}

	private MongoDatabase createMongoClient() {
		Properties mongoConfig = new Properties();
		try {
			mongoConfig.load(new FileInputStream(new File(configurationFolder + "/" + MONGO_CONFIG_FILE)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		MongoClient mongoClient = new MongoClient(
				new ServerAddress(mongoConfig.getProperty("host"), Integer.parseInt(mongoConfig.getProperty("port"))),
				MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
		bind(mongoClient).to(MongoClient.class);
		MongoDatabase mongoDatabase = null;
		String dbName = mongoConfig.getProperty("db");
		try {
			mongoDatabase = mongoClient.getDatabase(dbName);
		} catch (IllegalArgumentException e) {
			LOGGER.severe("The database " + dbName + " doesnt exist");
		}
		return mongoDatabase;
	}

	private Cache<Integer, Data> createCache() {
		return CacheFactory.getCacheFactory().createCache(new CacheConfiguration<Integer, Data>(
				new File(configurationFolder + "/" + CACHE_CONFIG_FILE), Integer.class, Data.class));
	}

	@Override
	protected void configure() {
		Cache<Integer, Data> cache = createCache();
		bind(new Store(createMongoClient(), cache)).to(Store.class);
		bind(cache).to(Cache.class);
	}

}