package clean.the.forest.area.infrastructure

import clean.the.forest.area.model.GeoPos
import clean.the.forest.shared.testing.TestClassification
import org.junit.jupiter.api.Tag
import spock.lang.Specification


@Tag(TestClassification.INTEGRATION)
class OpenWeatherProviderTest extends Specification {

    WeatherProvider sut

    def setup() {
        String apiKey = System.getenv('OPENWEATHER_APIKEY')
        sut = new OpenWeatherProvider(apiKey)
    }

    def "weather of #name by geo position"() {

        when:
        def weatherCondition = sut.byGeoPosition(new GeoPos(lat, lon)).block()

        then:
        weatherCondition == expectedWeather

        where:
        name        | lat    | lon    || expectedWeather
        "ipiñaburu" | 43.07d | -2.75d || "Clouds"
        "ibarra"    | 43.05d | -2.57d || "Clouds"
        "zegama"    | 42.97d | -2.29d || "Clouds"
    }

}

