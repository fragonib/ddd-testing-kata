# Design decisions

## Technologies used
- Gradle (for building project)
- Kotlin (for production code)
- Groovy (for testing with Spock)
- Cucumber (for functional test)
- Spring Cloud Contract (for contract testing)
- Hexagonal architecture
- Reactive modules

## Focused on
- Design patterns
- SOLID
- Testing techniques

## Requirements

1. Has at least JVM 11 to build and run the project 
   
2. You'd set up you own OpenWeather APIKEY as an environment variable

    ```shell
    export OPENWEATHER_APIKEY=%YOUR_API_KEY%
    ```
    
    You'll need this to define to: 
     - Run integration tests
     - Run complete application

# Running

To build the entire app with:

```shell
./gradlew build
```

To build the entire app but without running tests:

```shell
./gradlew clean build -x test -x contractTest
```

To publish artifacts (including app fat jar and server stubs):

```shell
./gradlew build publishToMavenLocal
```

To run integration tests (right now they depend on third parties):

```shell
./gradlew check test -DintegrationTests
```

To run contract tests:

```shell
./gradlew contractTest
```

To run functional tests (cucumber base):

```shell
./gradlew functionalTest
```

NOTE: Requires application running on localhost 

To run the entire app (in localhost):

```shell
./gradlew bootRun
```

Once up & running you can check and interact with the REST API using [this web UI](http://localhost:8080/swagger-ui.html):


## KNOWN errors

- Integration tests written using Spock are running during gradle `test` task instead of `integrationTest` task despite being correctly classified `@Tag(TestClassification.INTEGRATION)` (possible BUG on Spock - Gradle integration)
- `SpringCloudContract` isn't detecting contracts written in `Groovy` or `Kotlin`, only in YAML. Until fixing that, contracts are written with YAML limitations which is less expressive.

## What could be improved

- Docker file to containerize app
- GITLAB CI (providing _secrets_ in a secured way)
- Use Factories for test data fixtures
- Embrace variability (ie.: _Time provider_, Variable Weather conditions)
- Use `TestContainers` (to up & run application) and `WireMock` (to double third parties) whe running _Functional_ tests
- Review the possibility of [mixing Contract and API definition](https://springframework.guru/defining-spring-cloud-contracts-in-open-api/) (both in a single source of truth
