package clean.the.forest.weather.infrastructure

import clean.the.forest.weather.model.GeoPos
import clean.the.forest.weather.shared.TestClassification
import org.junit.jupiter.api.Tag
import spock.lang.Specification


@Tag(TestClassification.INTEGRATION)
class OpenWeatherProviderSpec extends Specification {

    WeatherProvider sut

    def setup() {
        String apiKey = System.getenv('OPENWEATHER_APIKEY')
        sut = new OpenWeatherProvider(apiKey)
    }

    def "weather of #name by geoposition"() {

        when:
        def weatherCondition = sut.reportWeatherByGeoPos(new GeoPos(lat, lon)).block()

        then:
        weatherCondition == expectedWheaterCondition

        where:
        name        | lat    | lon    || expectedWheaterCondition
        "ipiñaburu" | 43.07d | -2.75d || "Clouds"
        "ibarra"    | 43.05d | -2.57d || "Clouds"
        "zegama"    | 42.97d | -2.29d || "Clouds"
    }

}

