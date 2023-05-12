package clean.the.forest.area.infrastructure

import clean.the.forest.area.model.GeoPos
import clean.the.forest.shared.testing.Collaborator
import clean.the.forest.shared.testing.integration.CollaboratorsConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Subject

@SpringBootTest
@ActiveProfiles("isolated")
class OpenWeatherProviderIT extends CollaboratorSpec {

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
