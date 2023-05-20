package clean.the.forest.area.contract

import clean.the.forest.area.application.AddAreaUseCase
import clean.the.forest.area.infrastructure.AreaController
import clean.the.forest.area.application.ConflictWithExistingArea
import clean.the.forest.area.model.Area
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [AreaController::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["server.port=0"])
@EnableAutoConfiguration
abstract class CrudBase {

    @MockBean
    private lateinit var addAreaUseCase: AddAreaUseCase

    @LocalServerPort
    var port = 0

    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"

        val newArea = Area("new area", 40.0, -2.0, "ES")
        `when`(addAreaUseCase.addArea(newArea))
            .thenReturn(Mono.just(newArea))

        val area = Area("existing area", 40.0, -2.0, "ES")
        `when`(addAreaUseCase.addArea(area))
            .thenReturn(Mono.error(ConflictWithExistingArea("There is yet known area called [${area.name}]")))
    }

}
