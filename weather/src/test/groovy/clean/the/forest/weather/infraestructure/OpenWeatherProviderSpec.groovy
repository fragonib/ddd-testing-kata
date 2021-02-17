package clean.the.forest.weather.infraestructure

import clean.the.forest.weather.model.GeoPos
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import spock.lang.Specification


@Tag("integration")
class OpenWeatherProviderSpec extends Specification {

    OpenWeatherProvider provider

    def setup() {
        String apiKey = System.getenv('OPENWEATHER_APIKEY')
        provider = new OpenWeatherProvider(apiKey)
    }

    @Test
    def "weather of #name by geoposition"() {

        when:
        def weatherCondition = provider.reportWeatherByGeoPos(new GeoPos(lat, lon)).block()

        then:
        weatherCondition == expectedWheaterCondition

        where:
        name        | lat    | lon    || expectedWheaterCondition
        "ipiñaburu" | 43.07d | -2.75d || "Clouds"
        "ibarra"    | 43.05d | -2.57d || "Clouds"
        "zegama"    | 42.97d | -2.29d || "Clouds"
    }

}

