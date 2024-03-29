package clean.the.forest.area.infrastructure

import clean.the.forest.area.application.WeatherGateway
import clean.the.forest.area.domain.GeoPos
import clean.the.forest.shared.testing.integration.Collaborator
import clean.the.forest.shared.testing.integration.SprinDefinedCollaboratorSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Subject

@SpringBootTest
@ActiveProfiles("isolated")
class OpenWeatherGatewayIT extends SprinDefinedCollaboratorSpec {

    @Subject
    WeatherGateway sut

    def setup() {
        def openWeatherMockBaseUrl = collaboratorLifecycle.collaboratorMock(Collaborator.OPEN_WEATHER.literal).baseUrl()
        def openWeatherApiKey = 'dummy_api_key'
        sut = new OpenWeatherGateway(openWeatherMockBaseUrl, openWeatherApiKey)
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
