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
@ContextConfiguration(classes = [TestConfig])
@ActiveProfiles("isolated")
class OpenWeatherProviderIT extends CollaboratorSpec {

    @Autowired
    CollaboratorsConfig collaboratorsConfig

    @Subject
    WeatherProvider sut

    def setup() {
        super.setupO(collaboratorsConfig)
        def openWeatherMock = collaboratorLifecycle.collaboratorMock(Collaborator.OPEN_WEATHER.literal)
        def open_weather_api_key = 'dummy_api_key'
        sut = new OpenWeatherProvider(openWeatherMock.baseUrl(), open_weather_api_key)
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

    @Configuration
    static class TestConfig {
        @Bean
        CollaboratorsConfig collaboratorsConfig(Environment env) {
            Binder.get(env).bindOrCreate('collaborators-config', CollaboratorsConfig)
        }
    }

}
