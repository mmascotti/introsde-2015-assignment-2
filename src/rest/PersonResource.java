package rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import model.Person;
import transfer.PersonBean;
import transfer.mapper.Mapper;
import dao.Dao;

public class PersonResource {
	private int person_id;
	
    public PersonResource() {}
    
	public PersonResource(int id) {
		this.person_id = id;
	}

	/**
	 * Updates the person identified by {@link #person_id}.
	 * @param person values to store
	 * @return
	 */
	@PUT
    @Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    public Response updatePerson(PersonBean person) {
    	Person p = Dao.getPersonById(person_id);
    	if (p == null)
    		return Response.status(Status.NOT_FOUND).build();
    	
    	p.setFirstname(person.getFirstname());
    	p.setLastname(person.getLastname());
    	p.setBirthdate(person.getBirthdate());
    	Dao.savePerson(p, true);
    	
    	PersonBean ret = Mapper.createPersonBean(p);
		return Response.ok(ret).build();     
    }
    
	/**
	 * Deletes the person identified by {@link #person_id}. 
	 * @return
	 */
    @DELETE
    public Response deletePerson() {
    	Person p = Dao.getPersonById(person_id);
    	if (p == null)
    		return Response.status(Status.NOT_FOUND).build();
    	Dao.deletePerson(person_id);
		return Response.ok().build();
    }
    
    /**
     * @return the person identified by {@link #person_id}.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
    public Response getPerson() {
        Person p = Dao.getPersonById(person_id);
        
        if (p == null)
        	return Response.status(Status.NOT_FOUND).build();
        
        PersonBean pb = Mapper.createPersonBean(p);
        return Response.ok(pb).build();
    }
    
    /**
     * Creates a new {@link HealthprofileResource} for the person identified by {@link #person_id} and the specified measuretype.
     * @param measuretype
     * @return
     */
    @Path("/{measuretype}")
    public HealthprofileResource getPerson(@PathParam("measuretype") String measuretype) {
        return new HealthprofileResource(measuretype, person_id);
    }
}