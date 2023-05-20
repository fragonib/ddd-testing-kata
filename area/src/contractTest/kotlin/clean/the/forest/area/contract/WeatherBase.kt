package clean.the.forest.area.contract

import clean.the.forest.area.application.ReportWeatherOfParticularAreaUseCase
import clean.the.forest.area.application.ReportWeatherOfAllKnownAreasUseCase
import clean.the.forest.area.infrastructure.WeatherController
import clean.the.forest.area.domain.Area
import clean.the.forest.area.domain.WeatherReport
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
    private lateinit var reportParticularAreaUseCase: ReportWeatherOfParticularAreaUseCase

    @LocalServerPort
    var port = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"

        Mockito.`when`(reportAllKnownAreasUseCase.report())
            .thenReturn(Flux.just(
                WeatherReport(
                    area = Area("One area", 38.72, -0.53, "ES"),
                    weatherCondition = "Clouds",
                    date = LocalDateTime.now()
                ),
                WeatherReport(
                    area = Area("Other area", 40.23, -0.29, "ES"),
                    weatherCondition = "Clear",
                    date = LocalDateTime.now()
                )

            ))
    }

}
