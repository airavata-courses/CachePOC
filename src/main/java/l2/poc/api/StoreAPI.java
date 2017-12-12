package l2.poc.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import l2.poc.utils.Data;
import l2.poc.utils.Store;

@Path("store")
public class StoreAPI {
		private Store store=Store.getStore();
		
		
		@Path("/put")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.TEXT_PLAIN)
		@POST
		public String storeEntry(@QueryParam("cache") boolean writeToCache,Data data) {
			return store.addData(data,writeToCache);
		}
		
		@Path("/delete")
		@DELETE
		public void deleteEntry(@QueryParam(value = "key") String key) {
			store.deleteData(key);
		}
		
		@Path("/get")
		@Produces(MediaType.APPLICATION_JSON)
		@GET
		public Data getEntry(@QueryParam(value = "key") String key) throws InterruptedException {
			return store.getData(key);
		}
		
		@Path("/get/all")
		@Produces(MediaType.APPLICATION_JSON)
		@GET
		public Map<String, Data> getAll() throws InterruptedException {
			return store.getAllData();
		}
}
