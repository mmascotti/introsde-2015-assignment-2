package client;

import java.util.Iterator;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import transfer.HistoryBean;
import transfer.HistoryMeasureBean;
import transfer.MeasuretypeList;
import transfer.PeopleList;
import transfer.PersonBean;

/**
 * This class has the main method to execute the tests described in the assignment instructions 
 * {@link https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/assignments/assignment-2}. The methods 
 * <code> test1(...) </code> to <code> test12(...) </code> implement the tests described in  "Part 3 - The Client".
 */
public class TestClient {
	
	private static final String ERROR = "ERROR";
	private static final String OK = "OK";
	private static final String NO_RESULT = " - ";
	
	final static String XML = MediaType.APPLICATION_XML;
	final static String JSON = MediaType.APPLICATION_JSON;
	
	static Request xml_request;
	static Request json_request;
	
	//initialized in test 1, needed in test 2, 7 and 9
	static int [] first_last_t1_xml = new int [2];
	static int [] first_last_t1_json  = new int [2];
	//initialized in test 2, needed in test 3
	static PersonBean first_person_xml;
	static PersonBean first_person_json;
	
	//initialized in test 4, needed in test 5
	static int id_person_t4_xml;
	static int id_person_t4_json;
	
	//initialized in test 6, needed in test 7, 9 and 10
	static String [] measuretypes_xml = new String[2];
	static String [] measuretypes_json = new String[2];
	
	//initialized in test 7, needed in 8, 11 and 12
	static String mtype_xml;
	static String mtype_json;
	
	//initialized in test 7, needed in 8, 10, 11 and 12
	static int id_person_t7_xml;
	static int id_person_t7_json;
	
	//initialized in test 7, needed in test 8
	//initialized in test 9, needed in test 10
	static int mid_xml;
	static int mid_json;
	
	public static void main(String[] args) {
		if (args.length == 1){
			TestUtilities.resetDatabase(args[0]);
			System.exit(0);
		}
		
		if (args.length < 3){
			System.out.println("Usage: java client.TestClient [base_uri] [xml_logfile_path] [json_logfile_path]");
			System.exit(-1);
		}	
		
		TestUtilities testutils = new TestUtilities(args[0], args[1], args[2]);
		xml_request = new Request(XML, testutils);
		json_request = new Request(JSON, testutils);
		
		TestUtilities.resetDatabase(args[0]);
		
		test1(xml_request, first_last_t1_xml);
		test1(json_request, first_last_t1_json);
		
		test2(xml_request,first_last_t1_xml);
		test2(json_request, first_last_t1_json);
		
		test3(xml_request, "Xenia", first_person_xml);
		test3(json_request, "Jospeh", first_person_json);
		
		test4(xml_request);
		test4(json_request);
		
		test5(xml_request, id_person_t4_xml);
		test5(json_request, id_person_t4_json);
		
		test6(xml_request, measuretypes_xml);
		test6(json_request, measuretypes_json);
		
		test7(xml_request, first_last_t1_xml, measuretypes_xml);
		test7(json_request, first_last_t1_json, measuretypes_json);
		
		test8(xml_request, id_person_t7_xml, mtype_xml, mid_xml);
		test8(json_request, id_person_t7_json, mtype_json, mid_json);
		
		test9(xml_request,measuretypes_xml[0],first_last_t1_xml[0]);
		test9(json_request,measuretypes_json[0],first_last_t1_json[0]);
		
		test10(xml_request, id_person_t7_xml,measuretypes_xml[0],mid_xml);
		test10(json_request, id_person_t7_json,measuretypes_json[0],mid_json);
		
		test11(xml_request,id_person_t7_xml,mtype_xml);
		test11(json_request,id_person_t7_json,mtype_json);
		
		test12(xml_request,mtype_xml,id_person_t7_xml);
		test12(json_request,mtype_json,id_person_t7_json);
		
		testutils.close();
	}
	
	public static void test1(Request req, int[] persons) {
		
		Response resp = req.request01();
		PeopleList pl = resp.readEntity(PeopleList.class);

		int last_ind = pl.getPeople().size() - 1;
		persons[0] = pl.getPeople().get(0).getId();
		persons[1] = pl.getPeople().get(last_ind).getId();
		
		if (pl.getPeople().size() > 2)
			req.logResponse(resp, OK);
		else 
			req.logResponse(resp, ERROR);
	}
	
	public static void test2(Request req, int persons[]){
		Response resp = req.request02(persons[0]);
		PersonBean personbean = resp.readEntity(PersonBean.class);
		
		if (resp.getStatus() == 200 || resp.getStatus() == 202)
			req.logResponse(resp, OK);
		else  
			req.logResponse(resp, ERROR);
		
		//save person for further tests
		if (req.getMediatype().equals(XML))
			first_person_xml = personbean;
		else if (req.getMediatype().equals(JSON))
			first_person_json = personbean;
	}
	
	public static void test3(Request req, String newname, PersonBean pb){
		pb.setFirstname(newname);
		Response resp = req.request03(pb.getId(), pb);

		PersonBean changed = resp.readEntity(PersonBean.class);
		if (changed.getFirstname().equals(newname))
			req.logResponse(resp, OK);
		else
			req.logResponse(resp, ERROR);
	}
	
	public static void test4(Request req){
		String xml_body = 
				"<person>" +
					"<firstname>Chuck</firstname>" +
					"<lastname>Norris</lastname>" +
					"<birthdate>1945-01-01</birthdate>" +
					"<healthProfile>" +
						"<measureType>" +
							"<measure>Weight</measure>" +
							"<value>78.9</value>" + 
						"</measureType>" + 
						"<measureType>" +
							"<measure>Height</measure>" + 
							"<value>172.0</value>" + 
						"</measureType>" +
					 "</healthProfile>" +
				"</person>";
		
		String json_body = 
				"{" + 
						" \"firstname\" : \"Chuck\"," + 
						" \"lastname\" : \"Norris\"," + 
						" \"birthdate\" : \"1945-01-01\"," + 
						" \"healthProfile\" : {" + 
							"   \"measureType\" : [ {" + 
							"     \"measure\" : \"Weight\"," + 
							"     \"value\" : 78.9" + 
							"   }, {" + 
							"     \"measure\" : \"Height\"," + 
							"     \"value\" : 172.0" + 
							"   } ]" + 
							" }" + 
				"}";
		
		Response resp = null;
		if (req.getMediatype().equals(XML))
			resp = xml_request.request04(xml_body);
		else if (req.getMediatype().equals(JSON))
			resp = json_request.request04(json_body);
			
		PersonBean created = resp.readEntity(PersonBean.class);
		int save_created_id = created.getId(); 
		if (created.getId() > 0 && resp.getStatus() == 201)
			req.logResponse(resp, OK);
		else 
			req.logResponse(resp, ERROR);
		
		//save id for further tests
		if (req.getMediatype().equals(XML))
			id_person_t4_xml = save_created_id;
		else if (req.getMediatype().equals(JSON))
			id_person_t4_json = save_created_id;	
	}
	
	public static void test5(Request req, int created_test4){
		Response resp = req.request05(created_test4);
		req.logResponse(resp, NO_RESULT);

		resp = req.request02(created_test4);
		if (resp.getStatus() == 404)
			req.logResponse(resp, OK);
		else
			req.logResponse(resp, ERROR);
	}
	
	public static void test6(Request req, String [] result){
		Response resp = req.request09();
		MeasuretypeList mtypelist = resp.readEntity(MeasuretypeList.class);
		mtypelist.getMeasuretypes().toArray(result);

		if (result.length > 2)
			req.logResponse(resp, OK);
		else
			req.logResponse(resp, ERROR);
	}
	
	public static void test7(Request req, int persons[], String[] measuretypes) {
		int person_id = 0;
		String type = null;
		int mid = 0;
		
		boolean result_there = false;

		for (int p = 0; p < persons.length && !result_there; p++)
			for (int mt = 0; mt < measuretypes.length && !result_there; mt++) {
				Response resp = req.request06(persons[p], measuretypes[mt]);
				req.logResponse(resp, NO_RESULT);

				HistoryBean mb = resp.readEntity(HistoryBean.class);
				if (mb.getMeasures() != null && mb.getMeasures().size() > 0) {
					result_there = true;
					person_id = persons[p];
					type = measuretypes[mt];
					mid = mb.getMeasures().get(0).getMid();
				}
			}

		if (result_there)
			req.logResponse(null, OK);
		else
			req.logResponse(null, ERROR);
		
		//save data for further tests
		if (req.getMediatype().equals(XML)){
			id_person_t7_xml = person_id;
			mtype_xml = type;
			mid_xml = mid;
		}
		else if (req.getMediatype().equals(JSON)){
			id_person_t7_json = person_id;
			mtype_json = type;
			mid_json = mid;
		}
	}
	
	public static void test8(Request req, int personid, String measuretype, int mid){		
		Response resp = req.request07(personid, measuretype, mid);		
		if (resp.getStatus() == 200)
			req.logResponse(resp, OK);
		else
			req.logResponse(resp, ERROR);
	}
	
	public static void test9(Request req, String measuretype, int person_id){
		String body = null;
		int number_of_measures;
		
		//init 
		if (req.getMediatype().equals(XML)){
			body = "<measure> <value>72</value> <created>2011-12-09</created> </measure>";
		}
		else if (req.getMediatype().equals(JSON)){
			body = "{ \"value\" : 72, \"created\" : \"2011-12-09\"}";
		}
			
		//count measures before
		Response resp = req.request06(person_id, measuretype);
		req.logResponse(resp, NO_RESULT);
				
		HistoryBean mb = resp.readEntity(HistoryBean.class);
		number_of_measures = mb.getMeasures().size();
		
		//add one measure and save id for test 10
		resp = req.request08(person_id, measuretype, body);
		req.logResponse(resp, NO_RESULT);
		
		HistoryMeasureBean hmb = resp.readEntity(HistoryMeasureBean.class);
		int mid_for_test10 = hmb.getMid();
		
		//count measures after
		resp = req.request06(person_id, measuretype);
		mb = resp.readEntity(HistoryBean.class);
		
		if (mb.getMeasures().size() == number_of_measures + 1)
			req.logResponse(resp, OK);		
		else
			req.logResponse(resp, ERROR);
		
		//save data for further tests
		if (req.getMediatype().equals(XML))
			mid_xml = mid_for_test10;
		else if (req.getMediatype().equals(JSON))
			mid_json = mid_for_test10;
	}
	
	public static void test10(Request req, int person_id, String measuretype, int mid){
		String body = null;
		
		//init 
		if (req.getMediatype().equals(XML))
			body = "<measure> <value>90</value> <created>2011-12-09</created> </measure>";		
		else if (req.getMediatype().equals(JSON))
			body = "{ \"value\" : 90, \"created\" : \"2011-12-09\"}";
				
		Response resp = req.request10(person_id, measuretype, mid, body);
		req.logResponse(resp, NO_RESULT);
				
		resp = req.request06(person_id, measuretype);
		HistoryBean hb = resp.readEntity(HistoryBean.class);
		
		//find the updated measure
		HistoryMeasureBean updated_measure = null;
		Iterator<HistoryMeasureBean> it = hb.getMeasures().iterator();
		while (it.hasNext()) {
			updated_measure = it.next();
			if (updated_measure.getMid() == mid)
				break;
		}		
		
		//verify value
		if (updated_measure.getValue() == 90.0)
			req.logResponse(resp, OK);
		else
			req.logResponse(resp, ERROR);
	}
	
	public static void test11(Request req, int person_id, String measuretpye){
		
		long before = 1467151200000L;
		long after = 1323385200000L;
		
		Response resp = req.request11(person_id, measuretpye, before, after);
		HistoryBean hmb = resp.readEntity(HistoryBean.class);
		
		if (resp.getStatus() == 200 && hmb.getMeasures() != null && hmb.getMeasures().size() >= 1)
			req.logResponse(resp, OK);
		else
			req.logResponse(resp, ERROR);
	}
	
	public static void test12(Request req, String measuretpye, int person_id){
		double min = 80.0;
		double max = 110.0;
				
		Response resp = req.request12(person_id, measuretpye, min, max);
		
		HistoryBean hmb = resp.readEntity(HistoryBean.class);
		
		if (hmb.getMeasures() != null && hmb.getMeasures().size() > 1 && resp.getStatus() == 200)
			req.logResponse(resp, OK);
		else
			req.logResponse(resp, ERROR);
	}
}