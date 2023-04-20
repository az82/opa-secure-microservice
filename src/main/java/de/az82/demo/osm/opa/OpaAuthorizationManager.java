package de.az82.demo.osm.opa;

import de.az82.demo.osm.opa.OpaRequest.Input;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * OPA Authorization manager.
 */
@Slf4j
@Component
public class OpaAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private static final Pattern PATH_SEP_NORM_PATTERN = Pattern.compile("(^/)|(/$)");

    // For production, you might want to use the reactive web client
    private final RestTemplate client = new RestTemplate();
    private final String opaUrl;

    /**
     * Create a new instance.
     *
     * @param config OPA configuration
     */
    public OpaAuthorizationManager(OpaConfigProperties config) {
        this.opaUrl = config.getUrl();
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        var request = context.getRequest();

        return getOpaDecision(new Input(
                request.getMethod(),
                getRequestPath(request),
                getHeaders(request),
                authentication.get()));
    }

    private AuthorizationDecision getOpaDecision(Input input) {
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


    private AuthorizationDecision accessDenied(Input input) {
        log.warn("Access denied on {} for {}", input.getPath(), input.getAuth().getName());
        return new AuthorizationDecision(false);
    }

    private AuthorizationDecision accessGranted(Input input) {
        log.info("Access granted on {} for {}", input.getPath(), input.getAuth().getName());
        return new AuthorizationDecision(true);
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