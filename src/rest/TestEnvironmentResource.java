package rest;

import java.util.Calendar;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import transfer.PeopleList;
import transfer.mapper.Mapper;
import model.Measure;
import model.Person;
import dao.Dao;

@Path("/resetdatabase")
public class TestEnvironmentResource {
	
	@GET
    @Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML })
	public Response resetDatabse(){
		
		//delete all persons
		List<Person> persons = Dao.getAllPersons();
		for (int i = 0; i < persons.size(); i++)
			Dao.deletePerson(persons.get(i).getId());
		
		//add test data
		Person p= new Person();
		p.setFirstname("Alice");
		p.setLastname("A.");
		p.setBirthdate(newCal(14, 1, 1946));

		Measure m = new Measure();
		m.setMeasuretype("Weight");
		m.setValue(60.0);
		m.setCreated(newCal(21, 4, 1977));
		p.addMeasure(m);
		
		m = new Measure();
		m.setMeasuretype("Height");
		m.setValue(170.0);
		m.setCreated(newCal(21, 4, 1977));
		p.addMeasure(m);
		
		Dao.savePerson(p, false);
		
		p= new Person();
		p.setFirstname("Bob");
		p.setLastname("B.");
		p.setBirthdate(newCal(1, 8, 1956));

		m = new Measure();
		m.setMeasuretype("Weight");
		m.setValue(70.0);
		m.setCreated(newCal(16, 11, 1977));
		p.addMeasure(m);
		
		m = new Measure();
		m.setMeasuretype("Height");
		m.setValue(175.0);
		m.setCreated(newCal(28, 12, 1977));
		p.addMeasure(m);
		
		Dao.savePerson(p, false);
		
		p= new Person();
		p.setFirstname("Carol");
		p.setLastname("C.");
		p.setBirthdate(newCal(19, 9, 1956));

		m = new Measure();
		m.setMeasuretype("Weight");
		m.setValue(85.0);
		m.setCreated(newCal(30, 5, 1977));
		p.addMeasure(m);
		
		m = new Measure();
		m.setMeasuretype("Height");
		m.setValue(160.0);
		m.setCreated(newCal(1, 8, 1977));
		p.addMeasure(m);
		
		m = new Measure();
		m.setMeasuretype("step");
		m.setValue(1200.0);
		m.setCreated(newCal(10, 2, 1977));
		p.addMeasure(m);
		
		Dao.savePerson(p, false);
		
		persons = Dao.getAllPersons();
		PeopleList list = Mapper.createPeopleList(persons);
		return Response.ok(list).build();
	}
	
	private Calendar newCal(int day, int month, int year){
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		return c;
	}
}
