package clean.the.forest.area.infrastructure

import clean.the.forest.area.model.GeoPos
import clean.the.forest.shared.testing.Collaborator
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Subject

@SpringBootTest
@ActiveProfiles("isolated")
class OpenWeatherProviderIT extends BaseCollaboratorSpec {

    @Subject
    WeatherProvider sut

    def setup() {
        def openWeatherMockBaseUrl = collaboratorLifecycle.collaboratorMock(Collaborator.OPEN_WEATHER.literal).baseUrl()
        def openWeatherApiKey = 'dummy_api_key'
        sut = new OpenWeatherProvider(openWeatherMockBaseUrl, openWeatherApiKey)
    }

    def "weather of #name by geo position"() {

        when:
        def weatherCondition = sut.byGeoPosition(new GeoPos(lat, lon)).block()

        then:
        weatherCondition == expectedWeather

        where:
        name          | lat    | lon    || expectedWeather
        "Calderona"   | 39.67d | -0.43d || "Clear"
        "Mariola"     | 38.72d | -0.53d || "Clouds"
        "Penyagolosa" | 40.23d | -0.29d || "Clear"
    }

}
