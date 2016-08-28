package transfer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Transfer object for the healthprofile. <p> 
 * Example:
 * <pre>
 *&lt;healthProfile>
 *    &lt;measureType>
 *        &lt;measure>Height&lt;/measure>
 *        &lt;value>123&lt;/value>
 *    &lt;/measureType>
 *    &lt;measureType>
 *        &lt;measure>Weight&lt;/measure>
 *        &lt;value>67.8&lt;/value>
 *    &lt;/measureType>
 *&lt;/healthProfile>
 * </pre>
 */
@XmlRootElement(name = "healthProfile")
@XmlAccessorType(XmlAccessType.FIELD)
public class HealthprofileBean {
	
	@XmlElement(name="measureType")
	private List<MeasureBean> measures = new ArrayList<MeasureBean>();
	
	public List<MeasureBean> getMeasures() { return measures; }
	public void setMeasures(List<MeasureBean> measures) { this.measures = measures;	}
	
	@Override
	public String toString() {
		return measures.toString();
	}
}
