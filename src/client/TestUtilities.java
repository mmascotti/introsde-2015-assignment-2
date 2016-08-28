package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class TestUtilities {

	private final String BASE_URI;
	private final String XML_FILEPATH;
	private final String JSON_FILEPATH;
	WebTarget service;
	
	static PrintStream xml_logfile;
	static PrintStream json_logfile;
	
	/**
	 * Initializes the {@link TestUtilities} object with the parameters listed below.
	 * @param base_uri base uri of the server
	 * @param xml_logfile_path path to the logfile for the requests in XML
	 * @param json_logfile_path path to the logfile for the requests in JSON
	 */
	public TestUtilities(String base_uri, String xml_logfile_path, String json_logfile_path ) {
		this.BASE_URI = base_uri;
		this.XML_FILEPATH = xml_logfile_path;
		this.JSON_FILEPATH = json_logfile_path;
		
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		URI uri = UriBuilder.fromUri(BASE_URI).build();
		this.service = client.target(uri);
		
		try {
			xml_logfile = new PrintStream(new File(XML_FILEPATH));
			json_logfile = new PrintStream(new File(JSON_FILEPATH));
			xml_logfile.println("Server url: " + BASE_URI +"\n");
			json_logfile.println("Server url: " + BASE_URI + "\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the {@link PrintStream}s of the logfiles.
	 */
	public void close() {
		xml_logfile.close();
		json_logfile.close();		
	}
	
	/**
	 * Sends a request to the server.
	 * @param method the http method: GET, POST, PUT or DELETE
	 * @param path the path of the request
	 * @param query_params query parameters of the request. 
	 * 	<code> query_params[n][0]</code> is the name,  <code> query_params[n][1]</code> is the value for the n-th query parameter. 
	 * @param mediatype mediatype for the "Accept" header, can be {@link MediaType#APPLICATION_XML} or {@link MediaType#APPLICATION_JSON}
	 * @param content the content of the request
	 * @param req_number the request number as specified in 
	 * 	{@link https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/assignments/assignment-2}
	 * @return the {@link Response} of the request
	 */
	public Response makeRequest(String method, String path, String[][] query_params, String mediatype, Object content, int req_number){
		WebTarget webtarget = this.service.path(path);
		
		if (query_params != null)
			for (int i = 0; i < query_params.length; i++)
				webtarget = webtarget.queryParam(query_params[i][0], query_params[i][1]);		
						
		Invocation.Builder builder = webtarget.request().accept(mediatype);
	
		Response ret = null;		
		if (method.equals("GET"))
			ret = builder.get();
		else if (method.equals("POST"))
			ret = builder.post(Entity.entity(content, mediatype));
		else if (method.equals("PUT"))
			ret = builder.put(Entity.entity(content, mediatype));
		else if (method.equals("DELETE"))
			ret = builder.delete();
		else
			throw new RuntimeException("The method '" + method + "' is not valid for makeRequest(...).");
		
		//logging of the request
		String loguri = webtarget.getUri().getPath();
		String log_query =  webtarget.getUri().getQuery();
		
		if (log_query != null)
			loguri += "?" + log_query;
		
		logRequest(System.out, req_number, method, loguri, mediatype, mediatype);
		if (mediatype.equals(MediaType.APPLICATION_XML))
			logRequest(xml_logfile, req_number, method, loguri, mediatype, mediatype);
		else if (mediatype.equals(MediaType.APPLICATION_JSON))
			logRequest(json_logfile, req_number, method, loguri, mediatype, mediatype);
		else
			throw new RuntimeException("The media type '" + mediatype + "' is not valid for makeRequest(...).");
		
		ret.bufferEntity();
		return ret;
	}

	/**
	 * Logs the request in the following format: <code> Request #[NUMBER]: [HTTP METHOD] [URL] Accept: [TYPE] Content-type: [TYPE]</code>
	 * @param stream logfile stream
	 * @param req_number request number as specified in the assignment description
	 * @param http_method http method
	 * @param url request url
	 * @param accept "Accept" header
	 * @param content_type content type
	 */
	public void logRequest (PrintStream stream, int req_number, String http_method, String url, String accept, String content_type) {
		String format = "Request #%d: %s %s Accept: %s Content-type: %s\n"; 
		stream.format(format, req_number, http_method, url, accept, content_type);
		stream.flush();
	}

	/**
	 * Logs the result of a request.
	 * @param result user defined result of the request, such as OK, ERROR
	 * @param response {@link Response} of the request
	 * @param mediatype mediatype of the request
	 */
	public void logResponse (String result,  Response response, String mediatype) {
		logResponse(System.out, result, response);
				
		if (mediatype.equals(MediaType.APPLICATION_XML))
			logResponse(xml_logfile, result, response);
		else if (mediatype.equals(MediaType.APPLICATION_JSON))
			logResponse(json_logfile, result, response);		
	}
	
	/**
	 * Logs the user defined result (OK, ERROR, ...), the HTTP status code and the response body to the logfile stream.
	 * It uses the format as specified in the assignment description: <p>
	 * <code>  => Result: [RESPONSE STATUS = OK, ERROR] </code>  <br>  
	 * <code>  => HTTP Status: [HTTP STATUS CODE = 200, 404, 500 ...] </code>  <br>
	 * <code> [BODY]  </code>   
	 * @param stream stream of the logfile
	 * @param result userdefined result
	 * @param response {@link Response} of the request
	 */
	private void logResponse (PrintStream stream, String result,  Response response) {
		if (response == null){
			stream.format("=> Result: %s\n\n", result);
			stream.flush();
			return;
		}
		
		stream.format("=> Result: %s\n", result);
		stream.format("=> HTTP Status: %d\n", response.getStatus());
		
		String body = response.readEntity(String.class);
		if (body != null && !body.isEmpty()){
			if (response.getMediaType().toString().equals(MediaType.APPLICATION_XML))			
				prettyprint_XML(body, stream);
			else if (response.getMediaType().toString().equals(MediaType.APPLICATION_JSON))			
				prettyprint_JSON(body, stream);
			else
				stream.format("%s", body);
		}
		stream.append('\n');
		stream.flush();
	}

	/**
	 * Prints the JSON content with line breaks and indentations.
	 * @param source JSON content
	 * @param out stream of the file where the JSON content is printed to
	 */
	public void prettyprint_JSON(String source, PrintStream out) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode obj = mapper.readTree(source);
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false); //avoid closing the outputstream by writeValue()
			mapper.writeValue(out, obj);
			out.append('\n');
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints the XML content with line breaks and indentations.
	 * @param xml XML content 
	 * @param out stream of the file where the XML content is printed to
	 */
	public void prettyprint_XML(String xml, OutputStream out){
		try {
			OutputFormat output = OutputFormat.createPrettyPrint();
			Document doc = DocumentHelper.parseText(xml);
			XMLWriter xmlwriter = new XMLWriter(out, output);
			xmlwriter.write(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes all persons in the database without logging.
	 * @param serveruri 
	 */
	public static void resetDatabase(String serveruri) {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		URI uri = UriBuilder.fromUri(serveruri).build();
		WebTarget delete_service = client.target(uri);
		
		WebTarget webtarget = delete_service.path("/resetdatabase");							
		Invocation.Builder builder = webtarget.request().accept(MediaType.APPLICATION_JSON);	
		builder.get();
	}
}