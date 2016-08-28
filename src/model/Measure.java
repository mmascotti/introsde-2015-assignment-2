package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQuery(name="Measure.findAll", query="SELECT m FROM Measure m")
@Table(name="MEASURE")
public class Measure implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "MID")
	private int mid;

	private String measuretype;

	private Double value;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED")
	private Calendar created;

	@JoinColumn(name="PERSON_ID")
	private Person person;

	public Measure() {
		Calendar c = Calendar.getInstance();
		created = c;
	}

	public int getMid() { return this.mid;	}
	public String getMeasuretype() { return this.measuretype; }
	public Double getValue() { return this.value; }
	public Calendar getCreated() { return this.created;	}
	public Person getPerson() { return this.person; }

	public void setMid(int mid) { this.mid = mid; }
	public void setMeasuretype(String measuretype) { this.measuretype = measuretype; }
	public void setValue(Double value) { this.value = value; }
	public void setCreated(Calendar created) { this.created = created; }
	public void setPerson(Person person) { this.person = person; }
   
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("[" + this.mid + "] ");
    	
    	if (measuretype != null)
    		sb.append(this.measuretype.toString() + " ");
    	sb.append(this.value + " ");
    	if (created != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("(dd. MMM YYYY)");
    		sb.append(sdf.format(created.getTime()));
    	}
    	    	
    	if (person != null)
    		sb.append(person.getId());
    	return sb.toString();
    }
}