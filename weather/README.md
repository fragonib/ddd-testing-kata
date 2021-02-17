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

You need to set up you OpenWeather APIKEY in your environment

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