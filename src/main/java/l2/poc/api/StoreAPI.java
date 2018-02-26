package l2.poc.api;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import l2.poc.utils.Data;
import l2.poc.utils.Store;

@Path("store")
public class StoreAPI {
	@Inject
	private Store store;

	@Path("/put")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@POST
	public int storeEntry(Data data) {
		return store.addData(data);
	}

	@Path("/delete/{id}")
	@DELETE
	public void deleteEntry(@PathParam("id") int id) {
		store.deleteData(id);
	}

	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Data getEntry(@PathParam("id") int id) throws InterruptedException {
		return store.getData(id);
	}

	@Path("/get/all")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<Data> getAll() throws InterruptedException {
		return store.getAllData();
	}
}
