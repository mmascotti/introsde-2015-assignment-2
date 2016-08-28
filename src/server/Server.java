package server;

import java.net.InetAddress;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import rest.MeasuretypeResource;
import rest.PersonListResource;
import rest.TestEnvironmentResource;

public class Server {
	private static int port = 5700;

	public static void main(String[] args) throws Exception {

		String hostname = InetAddress.getLocalHost().getHostAddress();
		if (hostname.equals("127.0.0.1"))
			hostname = "localhost";
		
		if (args.length >= 1)
			port = Integer.parseInt(args[0]);		
		else
			System.out.println("Port not specified in arguments. Using default port: " + port);
		

		String url_str = String.format("http://%s:%d/", hostname, port);
		URI baseUrl = new URI(url_str);

		Set<Class<?>> resources = new HashSet<Class<?>>();
		resources.add(TestEnvironmentResource.class);
		resources.add(MeasuretypeResource.class);
		resources.add(PersonListResource.class);

		ResourceConfig rc = new ResourceConfig(resources);
		rc.register(JacksonFeature.class);
		JdkHttpServerFactory.createHttpServer(baseUrl, rc);
		
		System.out.println("Server started: " + baseUrl);
	}
}