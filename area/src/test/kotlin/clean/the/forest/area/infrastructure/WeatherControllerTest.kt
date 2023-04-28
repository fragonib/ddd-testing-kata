package clean.the.forest.area.infrastructure

import clean.the.forest.shared.testing.TestClassification
import clean.the.forest.shared.testing.MockitoHelper.anyObject
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import net.javacrumbs.jsonunit.core.Option
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono


@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [WeatherController::class])
@Import(value = [WeatherController::class])
@ContextConfiguration(classes = [AreaConfig::class])
@Tag(TestClassification.INTEGRATION)
class WeatherControllerTest {

    @MockBean
    lateinit var weatherProvider: WeatherProvider

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun whenReportAllKnownAreas_thenWeatherReportsExpected() {

        // Given
        `when`(weatherProvider.byGeoPosition(anyObject()))
            .thenReturn(Mono.just("Clouds"))

        // When
        val responseBody = webTestClient
            .get().uri("/weather").exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .returnResult()
            .responseBody!!.decodeToString()

        // Then
        assertThatJson(responseBody)
            .`when`(Option.IGNORING_ARRAY_ORDER)
            .whenIgnoringPaths("[*].date")
            .isEqualTo(
                """
                [
                    {
                        "area": {
                            "country": {
                                "code": "ES"
                            },
                            "name": "Mariola",
                            "position": {
                                "lat": 38.72,
                                "lon": -0.53
                            }
                        },
                        "weatherCondition": "Clouds",
                        "checkable": false
                    },
                    {
                        "area": {
                            "country": {
                                "code": "ES"
                            },
                            "name": "Penyagolosa",
                            "position": {
                                "lat": 40.23,
                                "lon": -0.29
                            }
                        },
                        "weatherCondition": "Clouds",
                        "checkable": false
                    },
                    {
                        "area": {
                            "country": {
                                "code": "ES"
                            },
                            "name": "Calderona",
                            "position": {
                                "lat": 39.67,
                                "lon": -0.43
                            }
                        },
                        "weatherCondition": "Clouds",
                        "checkable": false
                    }
                ]
            """
            )

        verify(weatherProvider, times(3)).byGeoPosition(anyObject())

    }

    @Test
    fun whenReportParticularAreaByName_thenWeatherReportExpected() {

        // Given
        `when`(weatherProvider.byGeoPosition(anyObject()))
            .thenReturn(Mono.just("Clouds"))

        // When
        val responseBody = webTestClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/weather")
                    .queryParam("location", "Calderona")
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .returnResult()
            .responseBody!!.decodeToString()

        // Then
        assertThatJson(responseBody)
            .whenIgnoringPaths("[*].date")
            .isEqualTo(
                """
                [
                    {
                        "area": {
                            "country": {
                                "code": "ES"
                            },
                            "name": "Calderona",
                            "position": {
                                "lat": 39.67,
                                "lon": -0.43
                            }
                        },
                        "weatherCondition": "Clouds",
                        "checkable": false
                    }
                ]
                """.trimIndent()
            )

        verify(weatherProvider, times(1)).byGeoPosition(anyObject())

    }

}
