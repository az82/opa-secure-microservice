package de.az82.demo.osm.opa;

import lombok.Data;
import org.springframework.security.core.Authentication;

import java.util.Map;

/**
 * Represents a request to OPA.
 *
 * An OPA request contains an arbitrarily structured input document.
 */
@Data
class OpaRequest {

    private final Input input;

    /**
     * OPA input document.
     */
    @Data
    static class Input {
        private final String method;
        private final String path;
        private final Map<String, String> headers;
        private final Authentication auth;
    }

}