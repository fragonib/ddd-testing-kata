package clean.the.forest.weather.contract

import clean.the.forest.weather.application.ReportAllKnownAreasUseCase
import clean.the.forest.weather.application.ReportParticularAreaUseCase
import clean.the.forest.weather.infrastructure.WeatherController
import clean.the.forest.weather.model.Area
import clean.the.forest.weather.model.WeatherCondition
import clean.the.forest.weather.model.WeatherReport
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Mono
import java.time.LocalDateTime


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [WeatherController::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["server.port=0"])
@EnableAutoConfiguration
abstract class CheckabilityBase {

    @MockBean
    private lateinit var reportAllKnownAreasUseCase: ReportAllKnownAreasUseCase

    @MockBean
    private lateinit var reportParticularAreaUseCase: ReportParticularAreaUseCase

    @LocalServerPort
    var port = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"
        stubResponseOnGivenArea("checkable_area", "Clear")
        stubResponseOnGivenArea("non_checkable_area", "Clouds")
    }

    private fun stubResponseOnGivenArea(areaName: String, weatherCondition: WeatherCondition) {
        `when`(reportParticularAreaUseCase.report(areaName))
            .thenReturn(Mono.just(WeatherReport(
                area = Area(areaName, 43.07, -2.75, "ES"),
                weatherCondition = weatherCondition,
                date = LocalDateTime.now()
            )))
    }

}