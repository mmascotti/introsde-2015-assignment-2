package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the PERSON database table.
 * 
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
		@NamedQuery(name = "Person.ById", query = "SELECT p FROM Person p where p.id = :id") })
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "ID")	
	private int id;
	
	private String firstname;

	private String lastname;

	@Temporal(TemporalType.DATE)
	@Column(name="BIRTHDATE")
	private Calendar birthdate;

	//bi-directional many-to-one association to Measure
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable
	  (
	      name="PERSON_MEASURE_ACTUAL",
	      joinColumns={ @JoinColumn(name="person_ID", referencedColumnName="ID") },
	      inverseJoinColumns={ @JoinColumn(name="measure_ID", referencedColumnName="MID") }
	  )
	@MapKey(name="measuretype")
	private Map<String, Measure> measures;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable
	  (
	      name="PERSON_MEASURE_HISTORY",
	      joinColumns={ @JoinColumn(name="person_ID", referencedColumnName="ID") },
	      inverseJoinColumns={ @JoinColumn(name="measure_ID", referencedColumnName="MID") }
	  )
	private List<Measure> history;

	public Person() {}

	public int getId() { return id;	}
	public String getFirstname() { return this.firstname; }
	public String getLastname() { return this.lastname; }
	public Calendar getBirthdate() { return this.birthdate; }
	public Map<String, Measure> getMeasures() { return this.measures; }
	public List<Measure> getHistory() { return history;	}

	public void setId(int id) {	this.id = id; }
	public void setFirstname(String firstname) { this.firstname = firstname; }
	public void setLastname(String lastname) { this.lastname = lastname; }
	public void setBirthdate(Calendar birthdate) { this.birthdate = birthdate; }
	public void setMeasures(Map<String, Measure> measures) { this.measures = measures; }
	public void setHistory(List<Measure> history) { this.history = history;	}
	
	public Measure addMeasure(Measure measure) { 
		measure.setPerson(this);
		if (this.measures == null)
			measures = new HashMap<String, Measure>();
		getMeasures().put(measure.getMeasuretype(), measure);	
		return measure;
	}

	public Measure removeMeasure(Measure measure) {
		getMeasures().remove(measure);
		measure.setPerson(null);
		return measure;
	}
	
	public Measure addHistoryMeasure(Measure measure) {
		measure.setPerson(this);
		if (this.measures == null)
			history = new ArrayList<Measure>();
		getHistory().add(measure);	
		return measure;
	}

	public Measure removeHIstoryMeasure(Measure measure) {
		getHistory().remove(measure);
		measure.setPerson(null);

		return measure;
	}
	    
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
    	
    	if (this.measures != null)
    		sb.append(" Healthprofile: " + measures.toString());
    	
    	if (this.history != null)
    		sb.append(" History: " + history.toString());
    	return sb.toString();
    }
}