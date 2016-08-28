package transfer;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import model.Measure;

/**
 * Representation of the {@link Measure} as history measure.<p>
 * Example: 
 * <pre>
 * &lt;measure>
 *    &lt;mid>222&lt;/mid>
 *    &lt;value>12&lt;/value>
 *    &lt;created>2000-02-03&lt;/created>
 * &lt;/measure>
 * </pre>
 */
@XmlRootElement(name="measure")
@XmlType(propOrder = { "mid", "value", "created" })
@XmlAccessorType(XmlAccessType.FIELD)
public class HistoryMeasureBean {
	
	@XmlElement(name="mid")
	private int mid;
	
	@XmlElement(name="value")
	private Double value;
	
	@XmlElement(name="created")
	private Calendar created;

	public int getMid() { return mid; }
	public Double getValue() { return value; }
	public Calendar getCreated() { return created; }

	public void setMid(int mid) { this.mid = mid; }
	public void setValue(Double value) { this.value = value; }
	public void setCreated(Calendar created) { this.created = created; }	
}
