package de.az82.demo.osm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * OPA secure microservice demo application.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class OsmApplication {

	public static void main(String[] args) {
		SpringApplication.run(OsmApplication.class, args);
	}

}
