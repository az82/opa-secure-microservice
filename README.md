# OPA Secure Microservice Demo

## Run and Develop Locally

### Requirements

* For local execution:
* Java 17+
* [opa](https://github.com/open-policy-agent/opa) engine
* [conftest](https://github.com/open-policy-agent/conftest) (For validating the application configuration)

This readme assumes that `opa` and `conftest` are installed in the workspace directory.

### Configuration

Create a `.env` file with the following content. Leave the providers you
do not want to use blank.

```bash
GITHUB_CLIENT_ID=YOUR-CREDENTIALS
GITHUB_CLIENT_SECRET=YOUR-CREDENTIALS

GOOGLE_CLIENT_ID=YOUR-CREDENTIALS
GOOGLE_CLIENT_SECRET=YOUR-CREDENTIALS
```

For GitHub, you need to create an OAuth2 client with the homepage URL `http://localhost:8080` 
and the authorization callback URL `http://localhost:8080/login/oauth2/code/github`.

For Google, the authorization callback URL must be `http://localhost:8080/login/oauth2/code/google`. For other providers
replace the part after `/code/` with the provider Id defined in `application.yaml`.

## Run and Develop With Gitpod

### Requirements

* [Gitpod](https://gitpod.io) account

### Configuration

* In the Gitpod user settings, add environment variables for the SSO providers you want to use.

| Variable Name        | Scope                         | Content          |
|----------------------|-------------------------------|------------------|
| GITHUB_CLIENT_ID     | az82/opa-secure-microservice  | YOUR-CREDENTIALS |
| GITHUB_CLIENT_SECRET | az82/opa-secure-microservice  | YOUR-CREDENTIALS |
| GOOGLE_CLIENT_ID     | az82/opa-secure-microservice  | YOUR-CREDENTIALS |
| GOOGLE_CLIENT_SECRET | az82/opa-secure-microservice  | YOUR-CREDENTIALS |


## Build

```bash
./gradlew build
```

## Run

Start OPA:

```bash
opa run -s src/main/rego/access-policy.rego
```

Start the service:

```bash
env $(cat .env | xargs) ./gradlew bootRun
```

The server will be listening at http://localhost:8080

### Validate the Application Configuration

```bash
conftest -p src/main/rego/config-policy.rego test src/main/resources/application.yaml
```

Expected output:

```bash
FAIL - src/main/resources/config-policy.yaml - Google is disallowed as an identity provider

1 test, 0 passed, 0 warnings, 1 failure, 0 exceptions
```

## Copyright & License

Copyright 2023 Andreas Zitzelsberger, released under the [MIT License](LICENSE).

The Gradle Wrapper is used under the Apache License, Version 2.0.
