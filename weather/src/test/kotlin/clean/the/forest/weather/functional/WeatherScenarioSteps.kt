package clean.the.forest.weather.functional

import clean.the.forest.shared.testing.ScenarioState
import clean.the.forest.shared.testing.Step.PARAM_EXP
import clean.the.forest.weather.model.AreaName
import clean.the.forest.weather.model.WeatherCondition
import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import io.cucumber.java8.PendingException


class WeatherScenarioSteps(val state: ScenarioState) : En {

    init {
        allAreasReportScenario()
        particularAreaCheckableScenario()
    }

    private fun allAreasReportScenario() {

        When("request report for all known areas") {
            println()
        }

        Then("report contains these areas:") { data: List<String> ->
            println(data)
        }

        Then("report contains weather condition:") { data: DataTable ->
            println(data)
        }

    }

    private fun particularAreaCheckableScenario() {

        When("request area condition {string}") { areaName: AreaName ->
            println(areaName)
        }

        Then("report weather is {string}") { weatherCondition: WeatherCondition ->
            println(weatherCondition)
        }

        Then("report checkable is {string}") { checkable: String ->
            println(checkable)
        }

    }

}