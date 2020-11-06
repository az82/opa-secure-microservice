package de.az82.demo.osm;

import de.az82.demo.osm.opa.OpaVoter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static java.util.Collections.singletonList;
import static org.springframework.security.web.csrf.CookieCsrfTokenRepository.withHttpOnlyFalse;


/**
 * Web security configuration.
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String OPA_URL = "http://localhost:8181/v1/data/http/authz/allow";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
// Delegate AuthZ decisions entirely to the OPA Voter
.authorizeRequests()
    .anyRequest()
    // By default this also covers anonymous access (with a dedicated "anonymous" principal)
    .authenticated()
    .accessDecisionManager(new UnanimousBased(singletonList(new OpaVoter(OPA_URL))))
// Expose the CSRF token as a cookie to JavaScript
// This will fail somewhere in the future as browsers will no longer allow it
// 
.and().csrf().csrfTokenRepository(withHttpOnlyFalse())
.and().logout().logoutSuccessUrl("/").permitAll()
// Render 401 unauthorized instead of the default redirect
.and().exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint())
.and().oauth2Login();
        // @formatter:on
    }

}
