package clean.the.forest.area.contract

import clean.the.forest.area.application.ReportWeatherOfAllKnownAreasUseCase
import clean.the.forest.area.application.ReportCheckabilityOfParticularAreaUseCase
import clean.the.forest.area.infrastructure.WeatherController
import clean.the.forest.area.model.Area
import clean.the.forest.area.model.WeatherCondition
import clean.the.forest.area.model.WeatherReport
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
    private lateinit var reportAllKnownAreasUseCase: ReportWeatherOfAllKnownAreasUseCase

    @MockBean
    private lateinit var reportCheckabilityOfParticularAreaUseCase: ReportCheckabilityOfParticularAreaUseCase

    @LocalServerPort
    var port = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"
        stubResponseOnGivenArea("checkable_area", "Clear")
        stubResponseOnGivenArea("non_checkable_area", "Clouds")
    }

    private fun stubResponseOnGivenArea(areaName: String, weatherCondition: WeatherCondition) {
        `when`(reportCheckabilityOfParticularAreaUseCase.report(areaName))
            .thenReturn(Mono.just(WeatherReport(
                area = Area(areaName, 43.07, -2.75, "ES"),
                weatherCondition = weatherCondition,
                date = LocalDateTime.now()
            )))
    }

}