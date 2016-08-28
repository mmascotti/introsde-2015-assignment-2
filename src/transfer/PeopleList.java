package transfer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper class for the the list of persons.
 * <pre> 
 *    &lt;people>
 *    	&lt person>
 *    	  ...
 *    	&lt;/person>
 *    	&lt;person>
 *    	 ...
 *    	&lt;/person>
 *    &lt;/people>
 *  </pre>
 */
@XmlRootElement(name="people")
@XmlAccessorType(XmlAccessType.FIELD)
public class PeopleList {
	@XmlElement(name="person")
	private List<PersonBean> people = new ArrayList<PersonBean>();
	
	public List<PersonBean> getPeople() { return people; }
	public void setPeople(List<PersonBean> people) { this.people = people; }
	
}
