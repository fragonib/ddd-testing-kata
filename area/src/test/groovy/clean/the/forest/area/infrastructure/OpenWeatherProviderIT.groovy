package clean.the.forest.area.infrastructure

import clean.the.forest.area.model.GeoPos
import clean.the.forest.shared.testing.integration.CollaboratorLifecycle
import clean.the.forest.shared.testing.integration.RestCollaborators
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
@ContextConfiguration(classes = [RestCollaboratorConfig])
@ActiveProfiles("isolated")
class OpenWeatherProviderIT extends Specification {

    static final String OPEN_WEATHER_COLLABORATOR = "openweather"

    @Autowired(required = true)
    RestCollaborators collaboratorsConfig

    @Subject
    WeatherProvider sut

    CollaboratorLifecycle collaboratorLifecycle

    def setup() {
        collaboratorLifecycle = new CollaboratorLifecycle(collaboratorsConfig)
        collaboratorLifecycle.setupCollaborators()
        sut = new OpenWeatherProvider(
                "http://localhost:${collaboratorsConfig.collaborators[OPEN_WEATHER_COLLABORATOR].port}",
                System.getenv('OPEN_WEATHER_MAP_API_KEY')
        )
    }

    def cleanup() {
        collaboratorLifecycle.teardownCollaborators()
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
