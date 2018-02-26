/**
 * 
 */
package l2.poc.cache.caffeine;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import l2.poc.utils.Data;
import l2.poc.utils.KeyValuePair;

/**
 * @author eldho
 *
 */
@Path("/cache")
public class CaffeineCacheService {

	@Path("/write")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean writeCache(KeyValuePair<String,Data> keyValuePair) {
		return true;
	}

	@Path("/delete")
	@DELETE
	public void deleteCache(String key) {
	}

	@Path("/read/all")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> readCache() {
		return null;
	}

}
