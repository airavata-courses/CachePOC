package l2.poc.utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import l2.poc.cache.Cache;

import static com.mongodb.client.model.Filters.*;

public class Store {
	private static final Logger LOGGER = Logger.getLogger(Store.class.getName());

	private AtomicInteger counter;
	private MongoCollection<Data> mongoCollection;
	private Cache<Integer, Data> cache;

	public Store(MongoDatabase mongoDatabase, Cache<Integer, Data> cache) {
		this.cache = cache;
		this.mongoCollection = mongoDatabase.getCollection("datas", Data.class);
		Data prev = this.mongoCollection.find().sort(new BasicDBObject("_id", -1)).first();
		if (prev != null) {
			counter = new AtomicInteger(prev.getId() + 1);
			LOGGER.info("Current Counter: " + counter.get());
		} else {
			counter = new AtomicInteger(0);
		}
	}

	public int addData(Data data) {
		int id = counter.getAndIncrement();
		data.setId(id);
		mongoCollection.insertOne(data);
		cache.write(id, data);
		return id;
	}

	public Data getData(int id) throws InterruptedException {
		return cache.read(id).orElseGet(() -> mongoCollection.find(eq("_id", id)).first());
	}

	public void deleteData(int id) {
		this.mongoCollection.deleteOne(eq("_id", id));
		cache.delete(id);
	}

	public List<Data> getAllData() throws InterruptedException {
		return StreamSupport.stream(this.mongoCollection.find().spliterator(), true).collect(Collectors.toList());
	}
}
