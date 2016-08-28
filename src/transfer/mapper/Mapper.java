package transfer.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import model.Measure;
import model.Person;

import org.dozer.DozerBeanMapper;

import transfer.HealthprofileBean;
import transfer.HistoryBean;
import transfer.HistoryMeasureBean;
import transfer.MeasureBean;
import transfer.PeopleList;
import transfer.PersonBean;

/**
 * Static methods for converting the objects of the database (package model) 
 * to the transfer objects (package transfer) and vice versa.
 */
public class Mapper {
	
	static DozerBeanMapper mapper;
	
	static {
		List<String> mappping_files = new ArrayList<String>();
		mappping_files.add("dozer.xml");

		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(mappping_files);
	}
	
	public static PersonBean createPersonBean(Person p){
		PersonBean ret = (PersonBean) mapper.map(p, PersonBean.class);
		Set<Entry<String, Measure>> e_set = p.getMeasures().entrySet();
		Iterator<Entry<String, Measure>> it = e_set.iterator();
		
		while (it.hasNext()){
			Entry<String, Measure> e = it.next();
			MeasureBean mb = createMeasureBean(e.getValue());
			ret.getHealthprofile().getMeasures().add(mb);
		}
		return ret;
	}
	
	public static Person createPerson(PersonBean p){
		Person ret = (Person) mapper.map(p, Person.class);
		
		if (p.getHealthprofile() != null && p.getHealthprofile().getMeasures() != null){
			List<MeasureBean> measures = p.getHealthprofile().getMeasures();
			for (int i = 0; i < measures.size(); i++){
				Measure m = createMeasure(measures.get(i));
				m.setPerson(ret);
				ret.addMeasure(m);
			}
		}		
		return ret;
	}
	
	public static Measure createMeasure(MeasureBean m){
		return (Measure) mapper.map(m, Measure.class);
	}
	
	public static Measure createMeasure(HistoryMeasureBean m){
		return (Measure) mapper.map(m, Measure.class);
	}
	
	public static MeasureBean createMeasureBean(Measure m){
		return (MeasureBean) mapper.map(m, MeasureBean.class);
	}

	public static HealthprofileBean createMeasureBean(Person p){
		return (HealthprofileBean) mapper.map(p, HealthprofileBean.class);
	}
	
	public static HistoryBean createHistoryBean(Person p){
		return (HistoryBean) mapper.map(p, HistoryBean.class);
	}
	
	public static HistoryMeasureBean createHistoryMeasureBean(Measure m){
		return (HistoryMeasureBean) mapper.map(m, HistoryMeasureBean.class);
	}
	
	public static HistoryBean createHistoryBean(List<Measure> history) {
		List<HistoryMeasureBean> historybeans = new ArrayList<HistoryMeasureBean>(history.size());
        
        for (int i = 0; i < history.size(); i++){
        	Measure hm = history.get(i);
        	HistoryMeasureBean hmb = Mapper.createHistoryMeasureBean(hm);
        	historybeans.add(hmb);
        }
               
        HistoryBean ret = new HistoryBean();
        ret.setMeasures(historybeans);
		return ret;
	}
	
	public static PeopleList createPeopleList(List<Person> people) {
		PeopleList ret = new PeopleList();
    	for (int i = 0; i < people.size(); i++){
    		PersonBean pb = Mapper.createPersonBean(people.get(i));
    		ret.getPeople().add(pb);
    	}
		return ret;
	}
}
