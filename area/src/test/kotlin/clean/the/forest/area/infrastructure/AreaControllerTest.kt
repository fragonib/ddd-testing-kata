package clean.the.forest.area.infrastructure

import clean.the.forest.shared.testing.TestClassification
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient


@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [AreaController::class])
@Import(value = [AreaController::class])
@ContextConfiguration(classes = [AreaConfig::class])
@Tag(TestClassification.INTEGRATION)
class AreaControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun whenAddingNewArea_thenRespondWithAreaCreated() {

        // When
        val responseBody = webTestClient
            .post().uri("/area")
            .bodyValue(AreaDTO(
                name = "new area",
                lat = 40.0,
                lon = -2.0,
                countryCode = "ES"
            ))
            .exchange()

        // Then
            .expectStatus().isCreated
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .returnResult()
            .responseBody!!.decodeToString()

        assertThatJson(responseBody)
            .whenIgnoringPaths("[*].date")
            .isEqualTo(
                """
                    {
                        "country": {
                            "code": "ES"
                        },
                        "name": "new area",
                        "position": {
                            "lat": 40.0,
                            "lon": -2.0
                        }
                    }
                """.trimIndent()
            )

    }

    @Test
    fun whenAddingAExistingArea_thenComplainWithConflict() {

        // When
        webTestClient
            .post().uri("/area")
            .bodyValue(AreaDTO(
                name = "Ipiñaburu",
                lat = 40.0,
                lon = -2.0,
                countryCode = "ES"
            ))
            .exchange()

        // Then
            .expectStatus().isEqualTo(HttpStatus.CONFLICT.value())

    }

}