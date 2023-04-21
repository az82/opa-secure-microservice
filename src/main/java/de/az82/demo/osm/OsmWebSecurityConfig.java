package de.az82.demo.osm;

import de.az82.demo.osm.opa.OpaAuthorizationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.csrf.CookieCsrfTokenRepository.withHttpOnlyFalse;


/**
 * Web security configuration.
 */
@Configuration
@EnableWebSecurity
class OsmWebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, OpaAuthorizationManager opaAuthorizationManager) throws Exception {
        // @formatter:off
        http
            .authorizeHttpRequests()
                // Delegate the authorization decision to OPA
                .anyRequest().access(opaAuthorizationManager)
            .and().csrf()
                .ignoringRequestMatchers("/login","/logout")
                // Expose the CSRF token as a cookie to JavaScript
                // This will fail sometime in the future as browsers will no longer allow it
                .csrfTokenRepository(withHttpOnlyFalse())
            .and().logout().logoutSuccessUrl("/")
            // Render 401 unauthorized instead of the default redirect
            .and().exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint())
            // Enable OAuth2 Login
            .and().oauth2Login();
        // @formatter:on
        return http.build();
    }

}
