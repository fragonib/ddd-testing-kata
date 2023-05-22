package clean.the.forest.area.infrastructure

import clean.the.forest.area.application.AddAreaUseCase
import clean.the.forest.area.application.ConflictWithExistingArea
import clean.the.forest.area.domain.Area
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import spock.lang.Specification


@WebFluxTest(AreaController)
@ContextConfiguration(classes = [AreaController])
// Not every annotation is supported by Spock, be careful:
// https://spockframework.org/spock/docs/2.3/module_spring.html
class AreaControllerIT extends Specification {

    @Autowired
    WebTestClient webTestClient

    @SpringBean
    AddAreaUseCase addAreaUseCase = Stub()

    def 'when adding a FRESH new area, then respond with Created'() {
        given:
        def area = new Area('Fresh new area', 40.0, -2.0, 'ES')
        addAreaUseCase.addArea(_ as Area) >> Mono.just(area)

        when:
        def exchange = webTestClient.post().uri('/area')
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue('''{
                    "countryCode": "ES",
                    "name": "Fresh new area",
                    "lat": 40.0,
                    "lon": -2.0
                }''')
                .exchange()

        then:
        exchange.expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .json('''{
                    "country": {"code": "ES"},
                    "name": "Fresh new area",
                    "position": {"lat": 40.0, "lon": -2.0}
                }''')

    }

    def 'when adding an EXISTING area, then complain with Conflict'() {
        given:
        addAreaUseCase.addArea(_ as Area) >>
                Mono.error(new ConflictWithExistingArea("Fresh new area"))

        when:
        def exchange = webTestClient.post().uri('/area')
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue('''{
                    "countryCode": "ES",
                    "name": "Fresh new area",
                    "lat": 40.0,
                    "lon": -2.0
                }''')
                .exchange()

        then:
        exchange.expectStatus().isEqualTo(HttpStatus.CONFLICT.value())
    }
}
