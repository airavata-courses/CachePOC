/**
 * 
 */
package l2.poc.api;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import l2.poc.cache.Cache;
import l2.poc.utils.Data;
import l2.poc.utils.KeyValuePair;
import l2.poc.utils.Store;

/**
 * @author eldho
 *
 */
@Path("/cache")
public class CacheAPI {
	
	private Cache<Data> cache=Store.getStore().getCache();
	
	@Path("/write")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean writeCache(KeyValuePair<Data> keyValuePair) {
		cache.write(keyValuePair.getKey(),keyValuePair.getValue());
		return true;
	}
	
	@Path("/delete")
	@DELETE
	public void deleteCache(String key) {
		cache.delete(key);
	}
	
	@Path("/read/all")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String,Data> readCache(){
		return cache.readAll();
	}
	
	

}
