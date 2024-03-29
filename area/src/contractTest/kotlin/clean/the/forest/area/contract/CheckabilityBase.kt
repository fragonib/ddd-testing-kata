package clean.the.forest.area.contract

import clean.the.forest.area.application.ReportWeatherOfParticularAreaUseCase
import clean.the.forest.area.application.ReportWeatherOfAllKnownAreasUseCase
import clean.the.forest.area.infrastructure.WeatherController
import clean.the.forest.area.domain.Area
import clean.the.forest.area.domain.WeatherCondition
import clean.the.forest.area.domain.WeatherReport
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import java.time.LocalDateTime


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [WeatherController::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["server.port=0"])
@EnableAutoConfiguration
abstract class CheckabilityBase {

    @MockBean
    private lateinit var reportAllKnownAreasUseCase: ReportWeatherOfAllKnownAreasUseCase

    @MockBean
    private lateinit var reportParticularAreaUseCase: ReportWeatherOfParticularAreaUseCase

    @LocalServerPort
    var port = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"
        stubResponseOnGivenArea("checkable_area", "Clear")
        stubResponseOnGivenArea("non_checkable_area", "Clouds")
        stubResponseOnNonKnownArea("not_known_area")
    }

    private fun stubResponseOnGivenArea(areaName: String, weatherCondition: WeatherCondition) {
        `when`(reportParticularAreaUseCase.report(areaName))
            .thenReturn(Mono.just(WeatherReport(
                area = Area(areaName, 39.67, -0.43, "ES"),
                weatherCondition = weatherCondition,
                date = LocalDateTime.now()
            )))
    }

    private fun stubResponseOnNonKnownArea(areaName: String,) {
        `when`(reportParticularAreaUseCase.report(areaName))
            .thenReturn(Mono.error(IllegalArgumentException("There is no area called [$areaName]")))
    }

}
