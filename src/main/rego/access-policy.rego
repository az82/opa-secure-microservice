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

# Add rules here
