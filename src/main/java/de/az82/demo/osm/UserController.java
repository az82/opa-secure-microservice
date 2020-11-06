package de.az82.demo.osm;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides information about the logged in user.
 */
@RestController
public class UserController {

    /**
     * Get the user name.
     *
     * @param user user object
     * @return user name
     */
    @GetMapping("/user/name")
    public String user(@AuthenticationPrincipal OAuth2User user) {
        return user.getAttribute("name");
    }

}
