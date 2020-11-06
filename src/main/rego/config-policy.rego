package main

# Policy for Spring application configuration

deny[msg] {
    # Fail if the Google OAuth2 client is used
    input.spring.security.oauth2.client.registration.google
    msg := "Google is disallowed as an identity provider"
}