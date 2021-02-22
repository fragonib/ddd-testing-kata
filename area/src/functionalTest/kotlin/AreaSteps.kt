package clean.the.forest.area.functional

import clean.the.forest.area.infrastructure.AreaDTO
import clean.the.forest.area.model.Area
import clean.the.forest.shared.testing.ScenarioState
import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate


class AreaSteps(
    private val scenarioState: ScenarioState,
    private val testClient: RestTemplate,
) : En {

    private val baseUrl = "http://localhost:8080/area"

    init {

        Given("following \"known areas\":") { data: DataTable ->
            val knownAreas = data.cells()
                .drop(/* header size */ 1)
                .map { (areaName, lat, lon, countryCode) ->
                    Area(areaName, lat.toDouble(), lon.toDouble(), countryCode)
                }
                .associateBy { it.name }
            scenarioState["knownAreas"] = knownAreas
        }

        When("add area {string} with data:") { newAreaLocator: String, data: DataTable ->

            // New area data
            val newArea = data.cells()
                .drop(/* header size */ 1)
                .map { (areaName, lat, lon, country) ->
                    AreaDTO(areaName, lat.toDouble(), lon.toDouble(), country)
                }
                .first()
            scenarioState[newAreaLocator] = newArea.toModel()

            // Add new Area
            try {
                val addResponse = testClient
                    .postForEntity(baseUrl, newArea, Area::class.java)
                scenarioState["addNewResponse"] = addResponse
            }
            catch(e: HttpStatusCodeException) {
                scenarioState["addNewResponse"] = ResponseEntity
                    .status(e.getRawStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
            }


        }

        Then("known areas should contain {string} and previous ones") { newAreaLocator: String ->

            // Assert new area has been added
            val responseEntity: ResponseEntity<Area> = scenarioState["addNewResponse"]!!
            assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.CREATED)

            //
            val newArea: Area = scenarioState[newAreaLocator]!!
            assertThat(responseEntity.body).isEqualTo(newArea)
        }

        Then("complain about previously existing area") {
            val jsonResponse: ResponseEntity<String> = scenarioState["addNewResponse"]!!
            assertThat(jsonResponse.statusCode).isEqualTo(HttpStatus.CONFLICT)
        }

    }

}
