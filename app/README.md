# Design decisions

## Technologies used
- Gradle (for building project)
- Kotlin (for production code)
- Groovy (for testing with Spock)

## Focused on
- Design patterns
- SOLID
- Testing

## Run

You need to set up you own OpenWeather APIKEY as an environment variable

```shell
export OPENWEATHER_APIKEY=%YOUR_API_KEY%
```

Then you can build and run all the tests:

```shell
./gradlew check
```

And run the entire app with:

```shell
./gradlew bootRun
```

## What could be improved

- Factories for test data fixtures
- GITLAB CI (secrets) 
- Clean dependencies
- Gradle group test selector 
- Refactor use cases
- Time provider
- Test edge cases
  