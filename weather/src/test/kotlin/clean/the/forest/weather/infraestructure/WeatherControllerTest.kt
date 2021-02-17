package clean.the.forest.weather.infraestructure

import clean.the.forest.weather.application.WeatherOfParticularAreaUseCase
import clean.the.forest.weather.model.Area
import clean.the.forest.weather.model.Country
import clean.the.forest.weather.model.GeoPos
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono


@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [WeatherController::class])
@ContextConfiguration(classes = [WeatherOfParticularAreaUseCase::class])
class WeatherControllerTest {

    @MockBean
    lateinit var areaRepository: AreaRepository

    @MockBean
    lateinit var weatherProvider: WeatherProvider

    @Autowired
    lateinit var testWebClient: WebTestClient

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
        testWebClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/weather")
                    .queryParam("location", locationString)
                    .build()
            }
            .exchange()

        // Then
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.name").isNotEmpty
            .jsonPath("$.id").isEqualTo(100)
            .jsonPath("$.weatherCondition").isEqualTo("Cloudy")
            .jsonPath("$.salary").isEqualTo(1000)

        verify(areaRepository, times(1)).findByName(locationString)
        verify(weatherProvider, times(1)).reportWeatherByGeoPos(expectedArea.position)

    }

}