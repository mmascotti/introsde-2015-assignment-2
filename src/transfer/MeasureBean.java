package transfer;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import model.Measure;

/**
 * Representation of the {@link Measure} as measure in the healthprofile.<p>
 * Example:
 * <pre>
 * &lt;measureType>
 *    &lt;measure>Height&lt;/measure>
 *    &lt;value>123&lt;/value>
 * &lt;/measureType>
 * </pre>
 */
@XmlRootElement(name="measureType")
@XmlType(propOrder = { "measure", "value" })
@XmlAccessorType(XmlAccessType.FIELD)
public class MeasureBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name="value")
	private Double value;

	@XmlElement(name="measure")
	private String measure;
	
	public MeasureBean() {}
	
	public Double getValue() { return value; }
	public String getMeasure() { return measure; }

	public void setValue(Double value) { this.value = value; }
	public void setMeasure(String measure) { this.measure = measure; }

	@Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	if (measure != null)
    		sb.append(this.measure + " ");
    	if (value != null)
    		sb.append(this.value + " ");    	
    	return sb.toString();
    }
}