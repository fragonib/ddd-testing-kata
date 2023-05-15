package clean.the.forest.area.functional

import clean.the.forest.area.model.Area
import clean.the.forest.area.model.AreaName
import clean.the.forest.area.model.WeatherCondition
import clean.the.forest.shared.testing.Collaborator
import clean.the.forest.shared.testing.functional.ScenarioState
import clean.the.forest.shared.testing.functional.WireMockLifeCycle.Companion.collaboratorLifecycle
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.jayway.jsonpath.JsonPath
import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.assertj.core.api.Assertions.assertThat
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URLEncoder

private const val GIVEN_WEATHER_CONDITIONS = "givenWeatherConditions"
private const val ACTUAL_JSON_WEATHER_REPORT = "actualJsonWeatherReport"

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
        Given("following \"weather conditions\":") { weatherConditionsTable: DataTable ->
            val givenWeatherConditions = weatherConditionsTable
                .extract { (areaName, weatherCondition) -> Pair(areaName, weatherCondition) }
                .associate { it }
            scenarioState[GIVEN_WEATHER_CONDITIONS] = givenWeatherConditions
            mockKnownAreasExternalWeatherProvider(givenWeatherConditions)
        }
    }

    private fun mockKnownAreasExternalWeatherProvider(expectedWeatherConditions: Map<String, String>) {

        val knownAreas: Map<AreaName, Area> = scenarioState["knownAreas"]
        val openWeatherCollaboratorMock = collaboratorLifecycle.collaboratorMock(Collaborator.OPEN_WEATHER.literal)

        knownAreas
            .map { (_, area) ->
                Triple(area.position.lat, area.position.lon, expectedWeatherConditions[area.name])
            }
            .forEach { (lat, lon, weatherCondition) ->
                openWeatherCollaboratorMock.stubFor(
                    get(urlMatching(".*/weather?lat=$lat&lon=$lon.*"))
                        .willReturn(aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withHeader("Cache-Control", "no-cache")
                            .withBody(javaClass.getResource("open_weather_response_template.json")!!.readText())
                            .withTransformerParameter("weatherCondition", weatherCondition)
                        ))
            }
    }

    private fun actions() {

        When("request report for all known areas") {
            val actualJsonWeatherReport = testClient
                .getForObject(baseUrl, String::class.java)!!
            scenarioState[ACTUAL_JSON_WEATHER_REPORT] = actualJsonWeatherReport
        }

        When("request report for area {string}") { areaName: AreaName ->
            val actualJsonWeatherReport = testClient
                .getForObject(UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("location", URLEncoder.encode(areaName, "UTF-8"))
                    .build(true)
                    .toUri(), String::class.java)!!
            scenarioState[ACTUAL_JSON_WEATHER_REPORT] = actualJsonWeatherReport
        }


    }

    private fun assertions() {

        Then("report should contain \"known areas\"") {
            val actualJsonWeatherReport: String = scenarioState[ACTUAL_JSON_WEATHER_REPORT]
            val knownAreas: Map<AreaName, Area> = scenarioState["knownAreas"]

            assertThatJson(actualJsonWeatherReport)
                .inPath("[*].area.name")
                .isArray
                .contains(*knownAreas.keys.toTypedArray())
        }

        Then("report should contain \"weather conditions\"") {
            val expectedWeatherConditions: Map<String, String> = scenarioState[GIVEN_WEATHER_CONDITIONS]
            val actualJsonWeatherReport: String = scenarioState[ACTUAL_JSON_WEATHER_REPORT]
            val reports = JsonPath.parse(actualJsonWeatherReport)
            val areaNames: List<String> = reports.read("$[*].area.name")
            val givenWeatherConditions: List<String> = reports.read("$[*].weatherCondition")

            areaNames.zip(givenWeatherConditions).forEach { (areaName, weatherCondition) ->
                assertThat(weatherCondition).isEqualTo(expectedWeatherConditions[areaName])
            }
        }

        Then("reported weather should be {string}") { expectedWeatherCondition: WeatherCondition ->
            val actualJsonWeatherReport: String = scenarioState[ACTUAL_JSON_WEATHER_REPORT]
            val reports = JsonPath.parse(actualJsonWeatherReport)
            val actualWeatherCondition = reports.read<String>("$[0].weatherCondition")

            assertThat(actualWeatherCondition).isEqualTo(expectedWeatherCondition)
        }

        Then("reported checkable should be {string}") { expectedCheckable: String ->
            val actualJsonWeatherReport: String = scenarioState[ACTUAL_JSON_WEATHER_REPORT]
            val reports = JsonPath.parse(actualJsonWeatherReport)
            val actualCheckable = reports.read<Boolean>("$[0].checkable")

            assertThat(actualCheckable).isEqualTo(expectedCheckable.toBoolean())
        }

    }

}
