package de.az82.demo.osm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Authentication entry point that renders a 401 unauthorized response.
 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(UNAUTHORIZED.value());

        response.setContentType("text/html");
        response.setCharacterEncoding(UTF_8.toString());

        response.getWriter().println(authException.getMessage());
    }

}
