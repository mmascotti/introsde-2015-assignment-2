package client;

import java.util.Formatter;

import javax.ws.rs.core.Response;

import transfer.PersonBean;

public class Request {
	String mediatype;
	TestUtilities testutilities;
	
	public Request(String mediatype, TestUtilities testutilities) {
		this.mediatype = mediatype;
		this.testutilities = testutilities;
	}

	public String getMediatype() {
		return mediatype;
	}
	
	/**
	 * GET /person
	 * @return
	 */
	Response request01(){
		Response resp = testutilities.makeRequest("GET", "/person", null, mediatype, null, 1);
		return resp;
	}
	
	/**
	 * GET /person/{person_id}
	 * @param person_id
	 * @return
	 */
	Response request02(int person_id){
		String url = "/person/" + person_id;
		Response resp = testutilities.makeRequest("GET", url, null, mediatype, null, 2);
		return resp;
	}
	
	/**
	 * PUT /person/{person_id}
	 * @param person_id
	 * @param content person data sent in the body
	 * @return
	 */
	Response request03(int person_id, PersonBean content){
		String url = "/person/" + person_id;
		Response resp = testutilities.makeRequest("PUT", url, null, mediatype, content, 3);
		return resp;
	}
	
	/**
	 * POST /person
	 * @param content person data sent in the body
	 * @return
	 */
	Response request04(String content){
		String url = "/person";
		Response resp = testutilities.makeRequest("POST", url, null, mediatype, content, 4);
		return resp;
	}
	
	/**
	 * DELETE /person/{person_id}
	 * @param person_id
	 * @return
	 */
	Response request05(int person_id){
		String url = "/person/" + person_id;
		Response resp = testutilities.makeRequest("DELETE", url, null, mediatype, null, 5);
		return resp;
	}
	
	/**
	 * GET /person/{person_id}/{measuretype}
	 * @param person_id
	 * @param measuretype
	 * @return
	 */
	Response request06(int person_id, String measuretype){
		Formatter urlformatter = new Formatter();
		String url = urlformatter.format("/person/%d/%s", person_id, measuretype).toString();
		urlformatter.close();
			
		Response resp = testutilities.makeRequest("GET", url, null, mediatype, null, 6);
		return resp;
	}
	
	/**
	 * GET /person/{person_id}/{measuretype}/{mid}
	 * @param person_id
	 * @param measuretype
	 * @param mid
	 * @return
	 */
	Response request07(int person_id, String measuretype, int mid){
		Formatter urlformatter = new Formatter();
		String url = urlformatter.format("/person/%d/%s/%d", person_id, measuretype, mid).toString();
		urlformatter.close();
			
		Response resp = testutilities.makeRequest("GET", url, null, mediatype, null, 7);
		return resp;
	}
	
	/**
	 * POST /person/{person_id}/{measuretpye}
	 * @param person_id
	 * @param measuretype
	 * @param content measure to be created
	 * @return
	 */
	Response request08(int person_id, String measuretype, String content){
		Formatter urlformatter = new Formatter();
		String url = urlformatter.format("/person/%d/%s", person_id, measuretype).toString();
		urlformatter.close();
			
		Response resp = testutilities.makeRequest("POST", url, null, mediatype, content, 8);
		return resp;
	}
	
	/**
	 * GET /measureTypes
	 * @return
	 */
	Response request09(){
		String url = "/measureTypes";
		Response resp = testutilities.makeRequest("GET", url, null, mediatype, null, 9);
		return resp;
	}
	
	/**
	 * PUT /person/{person_id}/{measuretype}/{mid}
	 * @param person_id
	 * @param measuretype
	 * @param mid
	 * @param content measure to be updated
	 * @return
	 */
	Response request10(int person_id, String measuretype, int mid, String content){
		Formatter urlformatter = new Formatter();
		String url = urlformatter.format("/person/%d/%s/%d", person_id, measuretype, mid).toString();
		urlformatter.close();
			
		Response resp = testutilities.makeRequest("PUT", url, null, mediatype, content, 10);
		return resp;
	}
	
	/**
	 * GET /person/{person_id}/{measuretype}?before={before_date}&after={after_date}
	 * @param person_id
	 * @param measuretype
	 * @param before_date
	 * @param after_date
	 * @return
	 */
	Response request11(int person_id, String measuretype, long before_date, long after_date){
		Formatter urlformatter = new Formatter();
		String url = urlformatter.format("/person/%d/%s", person_id, measuretype).toString();
		urlformatter.close();
		
		String [][] query_params = {{"before", "" + before_date},
									{"after", "" + after_date}};
							
		Response resp = testutilities.makeRequest("GET", url, query_params, mediatype, null, 11);
		return resp;
	}
	
	/**
	 * GET /person/{person_id}/{measuretype}?min={min}&max={max}
	 * @param person_id
	 * @param measuretype
	 * @param min
	 * @param max
	 * @return
	 */
	Response request12(int person_id, String measuretype, double min, double max){
		Formatter urlformatter = new Formatter();
		String url = urlformatter.format("/person/%d/%s", person_id, measuretype).toString();
		urlformatter.close();
		
		String [][] query_params = {{"min", "" + min},
									{"max", "" + max}};
							
		Response resp = testutilities.makeRequest("GET", url, query_params, mediatype, null, 11);
		return resp;
	}
	
	void logResponse(Response response, String result){
		testutilities.logResponse(result, response, this.mediatype);		
	}
}

