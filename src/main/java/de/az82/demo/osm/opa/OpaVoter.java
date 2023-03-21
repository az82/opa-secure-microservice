package de.az82.demo.osm.opa;

import de.az82.demo.osm.opa.OpaRequest.Input;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * OPA Authorization voter.
 */
@Slf4j
@Component
public class OpaVoter implements AccessDecisionVoter<FilterInvocation> {

    private static final Pattern PATH_SEP_NORM_PATTERN = Pattern.compile("(^/)|(/$)");

    // For production, you might want to use the reactive web client
    private final RestTemplate client = new RestTemplate();
    private final String opaUrl;

    /**
     * Create a new instance.
     *
     * @param config OPA configuration
     */
    public OpaVoter(OpaConfigProperties config) {
        this.opaUrl = config.getUrl();
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }

    @Override
    public int vote(Authentication auth, FilterInvocation filter, Collection<ConfigAttribute> attrs) {
        var request = filter.getRequest();

        return getOpaDecision(new Input(
                request.getMethod(),
                getRequestPath(request),
                getHeaders(request),
                auth));
    }

    private int getOpaDecision(Input input) {
        // For debugging. Do not log the authentication object in production
        log.debug("Get decision from OPA: {}", input);

        try {
            var response = requireNonNull(client.postForObject(
                    opaUrl,
                    new HttpEntity<>(new OpaRequest(input)),
                    OpaResponse.class));

            if (!response.isResult()) {
                return accessDenied(input);
            }
            return accessGranted(input);
        } catch (RestClientException e) {
            log.error("Error contacting OPA", e);
            return accessDenied(input);
        }
    }


    private int accessDenied(Input input) {
        log.warn("Access denied on {} for {}", input.getPath(), input.getAuth().getName());
        return ACCESS_DENIED;
    }

    private int accessGranted(Input input) {
        log.info("Access granted on {} for {}", input.getPath(), input.getAuth().getName());
        return ACCESS_GRANTED;
    }

    private static String getRequestPath(HttpServletRequest request) {
        return '/' + PATH_SEP_NORM_PATTERN.matcher(request.getRequestURI())
                .replaceAll("");
    }

    private static Map<String, String> getHeaders(HttpServletRequest request) {
        var headers = new HashMap<String, String>();

        for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
            String header = headerNames.nextElement();
            headers.put(header, request.getHeader(header));
        }

        return headers;
    }

}