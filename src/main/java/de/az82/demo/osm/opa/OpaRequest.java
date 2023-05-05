package de.az82.demo.osm.opa;

import org.springframework.security.core.Authentication;

import java.util.Map;

/**
 * Represents a request to OPA.
 * <p>
 * An OPA request contains an arbitrarily structured input document.
 */
record OpaRequest(Input input) {

    /**
     * OPA input document.
     */
    record Input(String method, String path, Map<String, String> headers, Authentication auth) {
    }

}