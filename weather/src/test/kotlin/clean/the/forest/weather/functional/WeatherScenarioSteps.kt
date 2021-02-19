package clean.the.forest.weather.functional

import clean.the.forest.shared.testing.ScenarioState
import clean.the.forest.weather.model.Area
import clean.the.forest.weather.model.AreaName
import clean.the.forest.weather.model.WeatherCondition
import com.jayway.jsonpath.JsonPath
import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.assertj.core.api.Assertions.assertThat


class WeatherScenarioSteps(private val scenarioState: ScenarioState) : En {

    init {
        allAreasReportScenario()
        particularAreaCheckableScenario()
    }

    private fun allAreasReportScenario() {

        When("request report for all known areas") {
            scenarioState["jsonReport"] = """
                [
                    {
                        "area": {
                            "country": {
                                "code": "ES"
                            },
                            "name": "Ibarra",
                            "position": {
                                "lat": 43.05,
                                "lon": -2.57
                            }
                        },
                        "weatherCondition": "Clear",
                        "checkable": false
                    },
                    {
                        "area": {
                            "country": {
                                "code": "ES"
                            },
                            "name": "Zegama",
                            "position": {
                                "lat": 42.97,
                                "lon": -2.29
                            }
                        },
                        "weatherCondition": "Drizzle",
                        "checkable": false
                    },
                    {
                        "area": {
                            "country": {
                                "code": "ES"
                            },
                            "name": "Ipiñaburu",
                            "position": {
                                "lat": 43.07,
                                "lon": -2.75
                            }
                        },
                        "weatherCondition": "Clouds",
                        "checkable": false
                    }
                ]
            """.trimIndent()
        }

        Then("report should contain \"known areas\"") {
            val jsonReport = scenarioState["jsonReport"] as String
            val knownAreas = scenarioState["knownAreas"] as Map<AreaName, Area>
            assertThatJson(jsonReport)
                .inPath("[*].area.name")
                .isArray
                .containsExactlyInAnyOrder(*knownAreas.keys.toTypedArray())
        }

        Then("report should contain \"weather condition\"") {
            val jsonReport = scenarioState["jsonReport"] as String
            val reports = JsonPath.parse(jsonReport)
            val areaNames: List<String> = reports.read("$[*].area.name")
            val weatherConditions: List<String> = reports.read("$[*].weatherCondition")

            areaNames.zip(weatherConditions).forEach { (areaName, weatherCondition) ->
                val expectedWeatherConditions = scenarioState["weatherConditions"] as Map<String, String>
                assertThat(weatherCondition).isEqualTo(expectedWeatherConditions[areaName])
            }
        }

    }

    private fun particularAreaCheckableScenario() {

        When("request area {string} report") { areaName: AreaName ->
            scenarioState["jsonReport"] = """
                [
                    {
                        "area": {
                            "country": {
                                "code": "ES"
                            },
                            "name": "Ibarra",
                            "position": {
                                "lat": 43.05,
                                "lon": -2.57
                            }
                        },
                        "weatherCondition": "Clear",
                        "checkable": false
                    }
                ]
            """.trimIndent()
        }

        Then("report weather is {string}") { expectedWeatherCondition: WeatherCondition ->
            val reports = JsonPath.parse(scenarioState["jsonReport"] as String)
            val actualWeatherCondition = reports.read<String>("$[0].weatherCondition")
            assertThat(actualWeatherCondition).isEqualTo(expectedWeatherCondition)
        }

        Then("report checkable is {string}") { expectedCheckable: String ->
            val reports = JsonPath.parse(scenarioState["jsonReport"] as String)
            val actualCheckable = reports.read<List<Boolean>?>("$[0].checkable")
            assertThat(actualCheckable).isEqualTo(expectedCheckable)
        }

    }

}
