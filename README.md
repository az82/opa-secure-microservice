# OPA Secure Microservice Demo

## Requirements

* Java 15+
* OPA [conftest](https://github.com/open-policy-agent/conftest) (For validating the application configuration)

## Configuration

Create a `.env` file with the following content. Leave the providers you
do not want to use blank.

```bash
GITHUB_CLIENT_ID=YOUR-CREDENTIALS
GITHUB_CLIENT_SECRET=YOUR-CREDENTIALS

GOOGLE_CLIENT_ID=YOUR-CREDENTIALS
GOOGLE_CLIENT_SECRET=YOUR-CREDENTIALS
```

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

Copyright 2020 Andreas Zitzelsberger, released under the [MIT License](LICENSE).

The Gradle Wrapper is used under the Apache License, Version 2.0.
