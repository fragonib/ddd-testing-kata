package clean.the.forest.weather.infraestructure

import clean.the.forest.weather.infraestructure.config.WeatherConfig
import clean.the.forest.weather.model.Area
import clean.the.forest.weather.model.Country
import clean.the.forest.weather.model.GeoPos
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono


@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [WeatherController::class])
@ContextConfiguration(classes = [WeatherConfig::class])
class WeatherControllerTest {

    @MockBean
    lateinit var areaRepository: AreaRepository

    @MockBean
    lateinit var weatherProvider: WeatherProvider

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun given_whenGetParticularAreaByLocation_thenWeatherReportExpected() {

        // Given
        val locationString = "ipiñaburu"
        val expectedArea = Area(
            name = "Ipiñaburu",
            position = GeoPos(lat = 43.07, lon = -2.75),
            country = Country(code = "ES")
        )
        `when`(areaRepository.findByName(locationString))
            .thenReturn(Mono.just(expectedArea))
        `when`(weatherProvider.reportWeatherByGeoPos(expectedArea.position))
            .thenReturn(Mono.just("Cloudy"))

        // When
        webTestClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/weather")
                    .queryParam("location", locationString)
                    .build()
            }
            .exchange()

            // Then
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.area").isMap
            .jsonPath("$.weatherCondition").isEqualTo("Clouds")
            .jsonPath("$.date").isNotEmpty

        verify(areaRepository, times(1)).findByName(locationString)
        verify(weatherProvider, times(1)).reportWeatherByGeoPos(expectedArea.position)

    }

}