# Design decisions

## Focus on
- Design patterns
- SOLID
- Testing

## Technologies used
- Gradle (for building project)
- Kotlin (for production code)
- Groovy (for testing with Spock)

## Run

You need to set up you own OpenWeather APIKEY as environment variable

```shell
export OPENWEATHER_APIKEY=%YOUR_API_KEY%
```

Then you can run the tests:

```shell
./gradlew check
```

Or the entire app with:

```shell
./gradlew bootRun
```

## What could be improved

- Factories for test data fixtures
- BDD 
- GITLAB CI
- Limpiar dependencias
- Gradle group test selector 