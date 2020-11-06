package de.az82.demo.osm.opa;


import lombok.Data;

/**
 * Response from OPA.
 * <p>
 * Here, only a simple allow/deny boolean is used.
 */
@Data
class OpaResponse {

    private boolean result;

}