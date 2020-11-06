package http.authz

# Default: No access

anon_paths := [
    "/",
    "/webjars/**"
    ]

auth_paths := [
    "/norris/fact",
    "/user/name",
    ]

allow {
    glob.match(anon_paths[_], ["/"], input.path)
}

allow {
    # Spring Boot idiosyncrasy: The anonymous user is "authenticated", but can
    # be identified by the principal "anonymousUser"
    input.auth.principal != "anonymousUser"
    glob.match(auth_paths[_], ["/"], input.path)
}