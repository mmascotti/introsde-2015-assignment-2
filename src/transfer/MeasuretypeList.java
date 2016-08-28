package transfer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The list of measuretypes. <p>
 * Example:
 * <pre>
 * &lt;measureTypes>
 *    &lt;measureType>weight&lt;/measureType>
 *    &lt;measureType>height&lt;/measureType>
 * &lt;/measureTypes>
 *
 */
@XmlRootElement(name="measureTypes")
@XmlAccessorType(XmlAccessType.FIELD)
public class MeasuretypeList {
	
	@XmlElement(name="measureType")
	private List<String> measuretypes = new ArrayList<String>();

	public MeasuretypeList() {}
	
	public MeasuretypeList(List<String> measuretypes) {
		super();
		this.measuretypes = measuretypes;
	}
	public List<String> getMeasuretypes() { return measuretypes; }
	public void setMeasuretypes(List<String> measuretypes) {this.measuretypes = measuretypes;}	
}
