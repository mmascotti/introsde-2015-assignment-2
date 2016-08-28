package transfer;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Transfer object for the history. <p>
 * Example:
 * <pre>
 *&lt;measureHistory> 
 *    &lt;measure>
 *        &lt;mid>111&lt;/mid>
 *        &lt;value>12.3&lt;/value>
 *        &lt;created>2000-01-02&lt;/created>
 *    &lt;/measure>
 *    &lt;measure>
 *        &lt;mid>222&lt;/mid>
 *        &lt;value>12&lt;/value>
 *        &lt;created>2000-02-03&lt;/created>
 *    &lt;/measure>
 *&lt;/measureHistory> 
 *</pre>
 */
@XmlRootElement(name="measureHistory")
@XmlAccessorType(XmlAccessType.FIELD)
public class HistoryBean {
	
	private List<HistoryMeasureBean> measures;
	
	public List<HistoryMeasureBean> getMeasures() {	return measures; }
	public void setMeasures(List<HistoryMeasureBean> measures) { this.measures = measures; }
}
