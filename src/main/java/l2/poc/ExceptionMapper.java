package l2.poc;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
	private static final Logger LOGGER = Logger.getLogger("Orchestration");
	private static long ERROR_ID = 100001;

	public ExceptionMapper() {
	}

	@Override
	public Response toResponse(Exception exception) {
		try {
			String errorID = "OE" + getERROR_ID();
			LOGGER.log(Level.SEVERE, "Error ID: " + errorID + "Exception Thrown: " + exception.getMessage(), exception);
			ErrorMessage errorMessage = new ErrorMessage(errorID, exception.getMessage());
			return Response.serverError().entity(errorMessage).build();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error Caught while handling another error", exception);
			return Response.serverError().build();
		}
	}

	public static long getERROR_ID() {
		return ++ERROR_ID;
	}

}
