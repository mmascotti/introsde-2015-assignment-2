package rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import transfer.MeasuretypeList;
import dao.Dao;

@Path("/measureTypes")
public class MeasuretypeResource {
	
	/**
	 * @return a list of all measure types that are in use
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public MeasuretypeList getMeasuretypes(){
		List<String> types = Dao.getMeasureTypes();
		MeasuretypeList ret = new MeasuretypeList(types);
		return ret;
	}
}
