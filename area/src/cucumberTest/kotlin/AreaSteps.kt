package clean.the.forest.area.functional

import clean.the.forest.shared.testing.ScenarioState
import clean.the.forest.area.model.Area
import clean.the.forest.area.model.AreaName
import clean.the.forest.area.model.WeatherCondition
import com.jayway.jsonpath.JsonPath
import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URLEncoder


class AreaSteps(
    private val scenarioState: ScenarioState,
) : En {

    private val testClient: RestTemplate = RestTemplate()
    private val baseUrl = "http://localhost:8080/area"

    init {

        Given("following \"known areas\":") { data: DataTable ->
            val knownAreas = data.cells()
                .drop(/* header size */ 1)
                .map { (areaName, lat, lon, country) ->
                    Area(areaName, lat.toDouble(), lon.toDouble(), country)
                }
                .associateBy { it.name }
            scenarioState["knownAreas"] = knownAreas
        }

        When("add area {string} with data:") { newAreaLocator: String, data: DataTable ->

            // New area data
            val newArea = data.cells()
                .drop(/* header size */ 1)
                .map { (areaName, lat, lon, country) ->
                    Area(areaName, lat.toDouble(), lon.toDouble(), country)
                }
                .first()
            scenarioState[newAreaLocator] = newArea

            // Request to add new area
            val addResponse = testClient
                .postForEntity(baseUrl, Area::class.java, Area::class.java)
            scenarioState["addNewResponse"] = addResponse
        }

        Then("known areas should contain {string} and previous ones") { newAreaLocator: String ->

            // New area has been added
            val jsonResponse: ResponseEntity<String> = scenarioState["addNewResponse"]!!
            assertThat(jsonResponse.statusCode).isEqualTo(201)

            //
            val newArea: Area = scenarioState[newAreaLocator]!!
            assertThat(jsonResponse.body).isEqualTo(newArea)
        }

        Then("complain about previously existing area") {
            val jsonResponse: ResponseEntity<String> = scenarioState["addNewResponse"]!!
            assertThat(jsonResponse.statusCode).isEqualTo(409)
        }

    }

}
