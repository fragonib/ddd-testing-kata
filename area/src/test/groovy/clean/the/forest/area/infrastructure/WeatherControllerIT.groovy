package clean.the.forest.area.infrastructure

import clean.the.forest.area.application.ReportWeatherOfAllKnownAreasUseCase
import clean.the.forest.area.application.ReportWeatherOfParticularAreaUseCase
import clean.the.forest.area.model.Area
import clean.the.forest.area.model.WeatherReport
import net.javacrumbs.jsonunit.core.Option
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson

@WebFluxTest(WeatherController)
@ContextConfiguration(classes = [WeatherController])
class WeatherControllerIT extends Specification {

    @Autowired
    WebTestClient webTestClient

    @SpringBean
    ReportWeatherOfAllKnownAreasUseCase allAreasUseCase = Mock()

    @SpringBean
    ReportWeatherOfParticularAreaUseCase particularAreaUseCase = Mock()

    def 'when reporting ALL KNOWN area, then weather reports as expected'() {
        given:
        allAreasUseCase.report() >> Flux.just(
                new WeatherReport(new Area('Mariola', 38.72, -0.53, 'ES'), 'Clouds'),
                new WeatherReport(new Area('Penyagolosa', 40.23, -0.29, 'ES'), 'Clear'),
                new WeatherReport(new Area('Calderona', 39.67, -0.43, 'ES'), 'Clouds')
        )

        when:
        def responseBody = webTestClient.get().uri('/weather')
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().returnResult().responseBody

        then:
        assertThatJson(new String(responseBody, 'UTF-8'))
                .when(Option.IGNORING_ARRAY_ORDER)
                .whenIgnoringPaths('[*].date')
                .isEqualTo('''
                    [
                        {
                            "area": {
                                "name": "Mariola",
                                "country": { "code": "ES" },
                                "position": { "lat": 38.72, "lon": -0.53 }
                            },
                            "weatherCondition": "Clouds",
                            "checkable": false
                        },
                        {
                            "area": {
                                "name": "Penyagolosa",
                                "country": { "code": "ES" },
                                "position": { "lat": 40.23, "lon": -0.29 }
                            },
                            "weatherCondition": "Clear",
                            "checkable": true
                        },
                        {
                            "area": {
                                "name": "Calderona",
                                "country": { "code": "ES" },
                                "position": { "lat": 39.67, "lon": -0.43 }
                            },
                            "weatherCondition": "Clouds",
                            "checkable": false
                        }
                    ]
                '''.stripIndent())

    }

    def "when reporting PARTICULAR area by name, then weather report as expected"() {
        given:
        particularAreaUseCase.report('Calderona') >> Mono.just(
                new WeatherReport(new Area('Calderona', 39.67, -0.43, 'ES'), 'Clouds')
        )

        when:
        def exchange = webTestClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                            .path('/weather')
                            .queryParam('location', 'Calderona')
                            .build()
                }
                .exchange()

        then:
        def responseBody = exchange
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().returnResult().responseBody
        assertThatJson(new String(responseBody, 'UTF-8'))
                .whenIgnoringPaths('[*].date')
                .isEqualTo('''
                    [
                        {
                            "area": {
                                "country": { "code": "ES" },
                                "name": "Calderona",
                                "position": { "lat": 39.67, "lon": -0.43 }
                            },
                            "weatherCondition": "Clouds",
                            "checkable": false
                        }
                    ]
                '''.stripIndent())
    }



    def "when reporting PARTICULAR area don't exists, then 404"() {
        given:
        particularAreaUseCase.report('Calderona') >> Mono.error(
                new AreaNotPresent('Calderona')
        )

        when:
        def exchange = webTestClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                            .path('/weather')
                            .queryParam('location', 'Calderona')
                            .build()
                }
                .exchange()

        then:
        exchange.expectStatus().isNotFound()
    }

}
