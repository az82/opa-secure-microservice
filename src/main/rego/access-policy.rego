package http.authz

# Default: Only authorized access
auth_paths := [
    "**",
    ]

anon_paths := [
    "/",
    "/index.html",
    "/webjars/**",
    ]

allow {
    glob.match(anon_paths[_], ["/"], input.path)
}

allow {
    # Spring Boot idiosyncrasy: The anonymous user is "authenticated", but can
    # be identified by the principal "anonymousUser"
    input.principal != "anonymousUser"
    glob.match(auth_paths[_], ["/"], input.path)
}