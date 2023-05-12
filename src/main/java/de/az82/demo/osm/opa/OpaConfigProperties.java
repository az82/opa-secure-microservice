package de.az82.demo.osm.opa;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for OPA.
 *
 * @param url OPA URL
 */
@ConfigurationProperties(prefix = "opa")
record OpaConfigProperties(String url) {

}
