package clean.the.forest.area.functional

import clean.the.forest.area.infrastructure.CreateAreaDTO
import clean.the.forest.area.domain.Area
import clean.the.forest.shared.testing.functional.ScenarioState
import clean.the.forest.shared.testing.functional.extract
import io.cucumber.datatable.DataTable
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import io.cucumber.java8.En

private const val GIVEN_KNOWN_AREAS = "givenKnownAreas"
private const val ADD_NEW_RESPONSE = "addNewResponse"

class AreaSteps(
    private val scenarioState: ScenarioState,
    private val testClient: RestTemplate,
) : En {

    private val baseUrl = "http://localhost:8080/area"

    init {

        Given("following \"known areas\":") { dataTable: DataTable ->
            scenarioState[GIVEN_KNOWN_AREAS] = dataTable.extract { (areaName, lat, lon, countryCode) ->
                    Area(areaName, lat.toDouble(), lon.toDouble(), countryCode)
                }
                .associateBy { it.name }
        }

        When("add area {string} with data:") { newAreaLocator: String, dataTable: DataTable ->

            // New area data
            scenarioState[newAreaLocator] = dataTable.extract { (areaName, lat, lon, countryCode) ->
                    CreateAreaDTO(areaName, lat.toDouble(), lon.toDouble(), countryCode)
                }
                .first()
                .toModel()

            // Add new Area
            try {
                scenarioState[ADD_NEW_RESPONSE] = testClient.postForEntity(
                    baseUrl,
                    dataTable.extract { (areaName, lat, lon, countryCode) ->
                        CreateAreaDTO(areaName, lat.toDouble(), lon.toDouble(), countryCode)
                    }
                        .first(),
                    Area::class.java
                )
            } catch (e: HttpStatusCodeException) {
                scenarioState[ADD_NEW_RESPONSE] =
                    ResponseEntity.status(e.statusCode.value()).headers(e.responseHeaders).body(e.responseBodyAsString)
            }
        }

        Then("known areas should contain {string} and previous ones") { newAreaLocator: String ->

            // Assert new area has been added
            val responseEntity: ResponseEntity<Area> = scenarioState[ADD_NEW_RESPONSE]
            assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.CREATED)

            //
            val newArea: Area = scenarioState[newAreaLocator]
            assertThat(responseEntity.body).isEqualTo(newArea)
        }

        Then("complain about previously existing area") {
            val jsonResponse: ResponseEntity<String> = scenarioState[ADD_NEW_RESPONSE]
            assertThat(jsonResponse.statusCode).isEqualTo(HttpStatus.CONFLICT)
        }

    }

}
