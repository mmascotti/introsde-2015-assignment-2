package rest;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import model.Person;
import transfer.PeopleList;
import transfer.PersonBean;
import transfer.mapper.Mapper;
import dao.Dao;

@Path("/person")
public class PersonListResource {

	/**
	 * @return a list of all persons in the database
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public PeopleList getPersons() {
		List<Person> people = Dao.getAllPersons();    	
		PeopleList ret = Mapper.createPeopleList(people);  
	    return ret;
	}

	/**
	 * Creates a new person in the database.
	 * @param person person to create
	 * @return
	 * @throws IOException
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	@Consumes({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public Response createPerson(PersonBean person) throws IOException {
		Person p = Mapper.createPerson(person);
		Dao.savePerson(p, false);
		
		PersonBean ret = Mapper.createPersonBean(p);
		return Response.ok(ret).status(Status.CREATED).build();       
	}

	@Path("/{personId}")
    public PersonResource getPerson(@PathParam("personId") int id) {
        return new PersonResource(id);
    }
}
