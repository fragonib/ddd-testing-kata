package clean.the.forest.area.contract

import clean.the.forest.area.application.ReportCheckabilityOfParticularAreaUseCase
import clean.the.forest.area.application.ReportWeatherOfAllKnownAreasUseCase
import clean.the.forest.area.infrastructure.WeatherController
import clean.the.forest.area.model.Area
import clean.the.forest.area.model.WeatherReport
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import java.time.LocalDateTime


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [WeatherController::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["server.port=0"])
@EnableAutoConfiguration
abstract class WeatherBase {

    @MockBean
    private lateinit var reportAllKnownAreasUseCase: ReportWeatherOfAllKnownAreasUseCase

    @MockBean
    private lateinit var reportCheckabilityOfParticularAreaUseCase: ReportCheckabilityOfParticularAreaUseCase

    @LocalServerPort
    var port = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"

        Mockito.`when`(reportAllKnownAreasUseCase.report())
            .thenReturn(Flux.just(
                WeatherReport(
                    area = Area("One area", 43.05, -2.57, "ES"),
                    weatherCondition = "Clouds",
                    date = LocalDateTime.now()
                ),
                WeatherReport(
                    area = Area("Other area", 42.97, -2.29, "ES"),
                    weatherCondition = "Clear",
                    date = LocalDateTime.now()
                )

            ))
    }

}