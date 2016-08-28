package rest;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import model.Measure;
import model.Person;
import transfer.HistoryBean;
import transfer.HistoryMeasureBean;
import transfer.MeasureBean;
import transfer.mapper.Mapper;
import dao.Dao;

public class HealthprofileResource {
	
	int person_id;
	String measuretype;

	/**
	 * Set the measuretype and the id of the person.
	 * @param measuretype
	 * @param person_id
	 */
	public HealthprofileResource(String measuretype, int person_id) {
		this.measuretype = measuretype;
		this.person_id = person_id;
	}

	/**
	 * Gets the history satisfying the specified range of dates or values. The request url can contain either the parameters
	 * 'before' and 'after' (to specify a range of dates) or the parameters 'min' and 'max' (to specify a range of values).
	 * @param before the upper limit for the date range
	 * @param after the lower limit for the date range
	 * @param min the lower limit for the value range
	 * @param max the upper limit for the value range
	 * @return the history satisfying the specified range
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public HistoryBean getHistoryOf(
			@DefaultValue("-1")  @QueryParam("before") long before,
			@DefaultValue("-1")  @QueryParam("after") long after,
			@DefaultValue("0.0") @QueryParam("min") double min,
			@DefaultValue(Double.MAX_VALUE + "") @QueryParam("max") double max)
	{
		List<Measure> history;
		if (before != -1 && after != -1) //date parameters are present
			history = Dao.getHistoryDateRange(person_id, measuretype, before, after);
		else if (min != 0.0 || max != Double.MAX_VALUE)
			history = Dao.getHistoryValueRange(person_id, measuretype, min, max);
		else
			history = Dao.getHistory(person_id, measuretype);
		
	    HistoryBean ret = Mapper.createHistoryBean(history);
	    return ret;
	}

	/**
	 * @param mid
	 * @return the measure with the specified measure id (mid)
	 */
	@GET
	@Path("/{mid}")
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public Response getByMeasureId( @PathParam("mid") int mid) {
	    Measure measure = Dao.getHistoryMeasure(mid);
	    
	    HistoryMeasureBean ret = new HistoryMeasureBean();
	    if (measure == null)
	    	return Response.status(Status.NOT_FOUND).build();        
	    else if (measure.getPerson().getId() == person_id && measure.getMeasuretype().equals(measuretype)) 
	    	ret  = Mapper.createHistoryMeasureBean(measure);
		else
			return Response.status(Status.NOT_FOUND).build();
	    return Response.ok(ret).build();
	}

	/**
	 * Sets a new value for the specified measure. The old value is archived in the measure history.
	 * @param measure 
	 * @return
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public Response updateMeasure( HistoryMeasureBean measure) {
		Person p = Dao.getPersonById(person_id);
	
		if (p == null)
			return Response.status(Status.NOT_FOUND).build();
		
		//archive existing measure, if exists on with that measure type
		Measure m = p.getMeasures().get(measuretype);
		if (m != null){
			p.getMeasures().remove(measuretype);
			p.getHistory().add(m);
		}
		
		//create new measure
		m = new Measure();
		m.setValue(measure.getValue());
		m.setCreated(measure.getCreated());
		m.setMeasuretype(measuretype);
		p.addMeasure(m);
		    	
		Dao.savePerson(p, true);
		
		m = p.getMeasures().get(measuretype);
		HistoryMeasureBean ret = Mapper.createHistoryMeasureBean(m);
		return Response.ok(ret).status(Status.CREATED).build();
	}

	/**
	 * Updates the measure identified by 'mid' with the specified value without archiving the old value in the measure history.
	 * @param mid
	 * @param measure
	 * @return
	 */
	@PUT
	@Path("/{mid}")
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public Response updateMeasureWithId( @PathParam("mid") int mid, HistoryMeasureBean measure)
	{
		Measure m = Dao.getMeasure(person_id,measuretype, mid);
		if (m != null){
			m.setValue(measure.getValue());
			m.setCreated(measure.getCreated());
			MeasureBean mb = Mapper.createMeasureBean(m);
			Dao.saveMeasure(m, true);
			return Response.ok(mb).build();
		}		
		return Response.status(Status.NOT_FOUND).build();		
	}
}
