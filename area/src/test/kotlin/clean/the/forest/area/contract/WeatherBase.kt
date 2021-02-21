package clean.the.forest.area.contract

import clean.the.forest.area.infrastructure.WeatherConfig
import clean.the.forest.area.infrastructure.WeatherController
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort


@SpringBootTest(classes = [WeatherConfig::class, WeatherController::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["server.port=0"])
@EnableAutoConfiguration
abstract class WeatherBase {

    @LocalServerPort
    var port = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost:" + port
    }

}