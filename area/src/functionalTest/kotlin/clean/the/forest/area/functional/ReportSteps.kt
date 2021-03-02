package clean.the.forest.area.functional

import clean.the.forest.area.model.Area
import clean.the.forest.area.model.AreaName
import clean.the.forest.area.model.WeatherCondition
import clean.the.forest.shared.testing.functional.ScenarioState
import clean.the.forest.shared.testing.functional.WireMockLifeCycle.Companion.wireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.jayway.jsonpath.JsonPath
import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.assertj.core.api.Assertions.assertThat
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URLEncoder


class ReportSteps(
    private val scenarioState: ScenarioState,
    private val testClient: RestTemplate,
) : En {

    private val baseUrl = "http://localhost:8080/weather"

    init {
        preconditions()
        actions()
        assertions()
    }

    private fun preconditions() {
        Given("following \"weather conditions\":") { data: DataTable ->
            val weatherConditions = data.cells()
                .drop(/* header size */ 1)
                .associate { (areaName, weatherCondition) -> Pair(areaName, weatherCondition) }
            scenarioState["weatherConditions"] = weatherConditions
            mockKnownAreasExternalWeatherProvider()
        }
    }

    fun mockKnownAreasExternalWeatherProvider() {

        val knownAreas: Map<AreaName, Area> = scenarioState["knownAreas"]!!
        val weatherConditions: Map<AreaName, WeatherCondition> =
            scenarioState["weatherConditions"]!!

        knownAreas
            .map { (name, area) ->
                Triple(area.position.lat, area.position.lon, weatherConditions[area.name])
            }
            .forEach { (lat, lon, weatherCondition) ->
                wireMockServer.stubFor(
                    get(urlMatching(".*/weather?lat=$lat&lon=$lon.*"))
                        .willReturn(aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withHeader("Cache-Control", "no-cache")
                            .withBody(javaClass.getResource("weather_ok.json").readText())
                            .withTransformers("response-template")
                            .withTransformerParameter("weatherCondition", weatherCondition)
                        ))
            }
    }

    private fun actions() {

        When("request report for all known areas") {
            val jsonReport = testClient
                .getForObject(baseUrl, String::class.java)!!
            scenarioState["jsonReport"] = jsonReport
        }

        When("request report for area {string}") { areaName: AreaName ->
            val jsonReport = testClient
                .getForObject(UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("location", URLEncoder.encode(areaName, "UTF-8"))
                    .build(true)
                    .toUri(), String::class.java)!!
            scenarioState["jsonReport"] = jsonReport
        }


    }

    private fun assertions() {

        Then("report should contain \"known areas\"") {
            val jsonReport: String = scenarioState["jsonReport"]!!
            val knownAreas: Map<AreaName, Area> = scenarioState["knownAreas"]!!

            assertThatJson(jsonReport)
                .inPath("[*].area.name")
                .isArray
                .contains(*knownAreas.keys.toTypedArray())
        }

        Then("report should contain \"weather conditions\"") {
            val expectedWeatherConditions: Map<String, String> =
                scenarioState["weatherConditions"]!!
            val jsonReport: String = scenarioState["jsonReport"]!!
            val reports = JsonPath.parse(jsonReport)
            val areaNames: List<String> = reports.read("$[*].area.name")
            val weatherConditions: List<String> = reports.read("$[*].weatherCondition")

            areaNames.zip(weatherConditions).forEach { (areaName, weatherCondition) ->
                assertThat(weatherCondition).isEqualTo(expectedWeatherConditions[areaName])
            }
        }

        Then("reported weather should be {string}") { expectedWeatherCondition: WeatherCondition ->
            val jsonReport: String = scenarioState["jsonReport"]!!
            val reports = JsonPath.parse(jsonReport)
            val actualWeatherCondition = reports.read<String>("$[0].weatherCondition")

            assertThat(actualWeatherCondition).isEqualTo(expectedWeatherCondition)
        }

        Then("reported checkable should be {string}") { expectedCheckable: String ->
            val jsonReport: String = scenarioState["jsonReport"]!!
            val reports = JsonPath.parse(jsonReport)
            val actualCheckable = reports.read<Boolean>("$[0].checkable")

            assertThat(actualCheckable).isEqualTo(expectedCheckable.toBoolean())
        }

    }

}
