package transfer;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import model.Person;

/**
 * Transfer object for {@link Person}.<p>
 * Example:
 * <pre>
 * &lt;person>
 *     &lt;firstname>Alice&lt;/firstname>
 *     &lt;lastname>Ecila&lt;/lastname>
 *     &lt;birthdate>1950-01-01&lt;/birthdate>
 *     &lt;healthProfile>
 *         &lt;weight>98.7&lt;/weight>
 *         &lt;height>175&lt;/height>
 *     &lt;/healthProfile>
 * &lt;/person>
 * </pre>
 */
@XmlRootElement(name="person")
@XmlType(propOrder = { "firstname", "lastname", "birthdate", "healthprofile"})
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private int id;	
	private String firstname;
	private String lastname;
	private Calendar birthdate;
	@XmlElement(name="healthProfile")
	private HealthprofileBean healthprofile;

	public PersonBean() {
		healthprofile = new HealthprofileBean();
	}

	public int getId() { return id;	}
	public String getFirstname() { return this.firstname;}
	public String getLastname() { return this.lastname; }
	public Calendar getBirthdate() { return this.birthdate; }
	public HealthprofileBean getHealthprofile() {return healthprofile;}

	public void setId(int id) { this.id = id; }
	public void setFirstname(String firstname) { this.firstname = firstname;}
	public void setLastname(String lastname) { this.lastname = lastname; }
	public void setBirthdate(Calendar birthdate) { this.birthdate = birthdate; }
	public void setHealthprofile(HealthprofileBean healthprofile) { this.healthprofile = healthprofile; }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("[" + this.id + "] ");
    	
    	if (this.firstname != null)
    		sb.append(this.firstname + " ");
    	
    	if (this.lastname != null)
    		sb.append(this.lastname + " ");
    	
    	if (birthdate != null){
    		SimpleDateFormat sdf = new SimpleDateFormat("dd. MMM YYYY");
    		sb.append(sdf.format(birthdate.getTime()));
    	}
    	
    	if (healthprofile != null)
    		sb.append(" " + healthprofile.toString());
    	return sb.toString();
    }
   }