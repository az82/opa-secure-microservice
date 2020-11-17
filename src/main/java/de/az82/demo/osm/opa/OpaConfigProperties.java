package de.az82.demo.osm.opa;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Configuration for OPA.
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "opa")
class OpaConfigProperties {

    private final String url;

    /**
     * Create a new instance.
     *
     * @param url URL pointing to an allow method, eg. {@code http://HOST/v1/data/SOME/PACKAGE/allow}
     */
    OpaConfigProperties(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
