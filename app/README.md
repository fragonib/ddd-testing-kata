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

# run

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

To run the entire app with:

```shell
./gradlew bootRun
```

To run integration tests (They depend on thrid parties):

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

## KNOW errors

- Integration tests written using Spock are running during `test` task inteead of `integrationTest` task despite being correctly classified `@Tag(TestClassification.INTEGRATION)`. (Possible Spock - Gradle bad integration)
- Spring cloud Contract isn't detecting contracts written in Groovy o Kotlin, only in YAML. Until these will be solved contract won't be very expressive

## What could be improved

- GITLAB CI (providing secrets in a secured way)
- Docker file to containerize app
- Factories for test data fixtures
- Embrace variability (ie.: Time provider, Variable Weather conditions)
- Use TestContainers or WireMock to execute Functional test
- Review the possibility of mixing contract definition + API definition (in single source of truth) 
