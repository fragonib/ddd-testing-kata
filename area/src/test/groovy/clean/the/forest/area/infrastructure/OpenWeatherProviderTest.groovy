package clean.the.forest.area.infrastructure

import clean.the.forest.area.model.GeoPos
import clean.the.forest.shared.testing.TestClassification
import org.junit.jupiter.api.Tag
import spock.lang.Specification


@Tag(TestClassification.INTEGRATION)
class OpenWeatherProviderTest extends Specification {

    WeatherProvider sut

    def setup() {
        String apiKey = System.getenv('OPEN_WEATHER_MAP_API_KEY')
        String baseUrl = "https://api.openweathermap.org/data/2.5"
        sut = new OpenWeatherProvider(baseUrl, apiKey)
    }

    def "weather of #name by geo position"() {

        when:
        def weatherCondition = sut.byGeoPosition(new GeoPos(lat, lon)).block()

        then:
        weatherCondition == expectedWeather

        where:
        name          | lat    | lon    || expectedWeather
        "Calderona"   | 39.67d | -0.43d || "Clouds"
        "Mariola"     | 38.72d | -0.53d || "Clouds"
        "Penyagolosa" | 40.23d | -0.29d || "Clouds"
    }

}

