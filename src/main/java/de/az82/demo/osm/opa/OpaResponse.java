package de.az82.demo.osm.opa;


/**
 * Response from OPA.
 * <p>
 * Here, only a simple allow/deny boolean is used.
 */
record OpaResponse(boolean result) {

}