package dao;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import model.Measure;
import model.Person;

public class Dao {
    
    private static EntityManagerFactory emf;
    private static EntityManager em;

    static {
        if (emf!=null) {
            emf.close();
        }
        emf = Persistence.createEntityManagerFactory("introsde_assignment2");
        em = emf.createEntityManager();
    }
    
    @Override
    protected void finalize() throws Throwable {
    	em.close();
    	emf.close();
    	super.finalize();
    }

    /**
     * Saves or updates the specified {@link Person}.
     * @param p 
     * @param update whether the existing person should be updated
     * @return
     */
    public static Person savePerson(Person p, boolean update) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		if (update)
			em.merge(p);
		else
			em.persist(p);	
		tx.commit();
		return p;
	}
	
    /**
     * Saves or updates the specified {@link Measure} 
     * @param m
     * @param update whether the existing measure should be updated
     * @return
     */
	public static Measure saveMeasure(Measure m, boolean update) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		if (update)
			em.merge(m);
		else
			em.persist(m);	
		tx.commit();
		return m;
	}

	/**
	 * 
	 * @return a list of all persons in the database
	 */
	@SuppressWarnings("unchecked")
	public static List<Person> getAllPersons(){
		Query q = em.createQuery("Select p from Person p");
		List<Person> ret = (List<Person>) q.getResultList();
		return ret;	
	}
	
	/**
	 * 
	 * @param id
	 * @return the person indentified by "id"
	 */
	public static Person getPersonById(int id){
		Person ret =  em.find(Person.class, id);
		return ret;	
	}


	/**
	 * Deletes the person identified by "id" from the database
	 * @param id
	 */
	public static void deletePerson(int id) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Person p = em.find(Person.class, id);
		if (p != null){
			em.remove(p);
			em.flush();
		}
		tx.commit();
	}
	
	/**
	 * @param mid
	 * @return the  measure identified by "mid".
	 */
	public static Measure getHistoryMeasure(int mid) {
		Measure ret = em.find(Measure.class, mid);
		return ret;
	}

	/**
	 * @return a list of all used measure types
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getMeasureTypes() {
		String query = 
				"select m.measuretype " +
				"from Measure m " +
				"group by m.measuretype";
		Query q = em.createQuery(query);
	
		List<String> ret = q.getResultList();
		return ret;
	}

	/**
	 * @param person_id
	 * @param type
	 * @return the history of the measuretype "type" of the person identified by "id"
	 */
	@SuppressWarnings("unchecked")
	public static List<Measure> getHistory(int person_id, String type) {
		String query = 
					"select m " +
					"from Measure m " +
					"where m.person.id = :id and m.measuretype = :type " +
					"order by m.created";
						
		Query q = em.createQuery(query);
		q.setParameter("id", person_id);
		q.setParameter("type", type);
	
		List<Measure> ret = q.getResultList();
		return ret;
	}
	
	/**
	 * Returns the history of the measuretype "type" of the person identified by "id" in the date range.
	 * @param person_id person id
	 * @param type measuretype
	 * @param before lower limit of the date range
	 * @param after upper limit of the date range
	 * @return the list of measures in the date range
	 */
	@SuppressWarnings("unchecked")
	public static List<Measure> getHistoryDateRange(int person_id, String type, long before, long after) {
		String query = 
					"select m " +
					"from Measure m " +
					"where m.person.id = :id and m.measuretype = :type " +
						"and m.created > :after and m.created < :before " +
					"order by m.created";
		Calendar c_before = Calendar.getInstance();
		c_before.setTimeInMillis(before);
		Calendar c_after = Calendar.getInstance();
		c_after.setTimeInMillis(after);
				
		Query q = em.createQuery(query);
		q.setParameter("id", person_id);
		q.setParameter("type", type);
		q.setParameter("after", c_after, TemporalType.TIMESTAMP);
		q.setParameter("before", c_before, TemporalType.TIMESTAMP);
		
		List<Measure> ret = q.getResultList();
		return ret;
	}
	
	/**
	 * Returns all {@link Measure}s which values are in the specified range for the specified person and measuretype.
	 * @param person_id id of the person
	 * @param type measuretype
	 * @param min lower limit of the measure value
	 * @param max upper limit of the measure value
	 * @return all {@link Measure}s which values are in the specified range for the specified person and measuretype
	 */
	@SuppressWarnings("unchecked")
	public static List<Measure> getHistoryValueRange(int person_id, String type, Double min, Double max) {
		String query = 
					"select m " +
					"from Measure m " +
					"where m.person.id = :id and m.measuretype = :type " +
						"and m.value < :max and m.value > :min " +
					"order by m.created";
						
		Query q = em.createQuery(query);
		q.setParameter("id", person_id);
		q.setParameter("type", type);
		q.setParameter("min", min);
		q.setParameter("max", max);
		
		List<Measure> ret = q.getResultList();
		return ret;
	}

	/**
	 * @param person_id
	 * @param measuretype
	 * @param mid
	 * @return the {@link Measure} identified by "mid" of type "measuretype" for the person identified by "person_id"
	 */
	@SuppressWarnings("unchecked")
	public static Measure getMeasure(int person_id, String measuretype, int mid) {
		String query = 
				"select m " +
				"from Measure m " +
				"where m.person.id = :pid and " +
					  "m.measuretype = :type and " +
					  "m.mid = :mid";
					
		Query q = em.createQuery(query);
		q.setParameter("pid", person_id);
		q.setParameter("type", measuretype);
		q.setParameter("mid", mid);
		
		List<Measure> ret = q.getResultList();
		return ret.get(0);
	}
}