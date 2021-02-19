package clean.the.forest.weather.functional

import clean.the.forest.shared.testing.ScenarioState
import io.cucumber.datatable.DataTable
import io.cucumber.java8.En


class BackgroundSteps(private val state: ScenarioState) : En {

    init {
        setupSteps()
    }

    private fun setupSteps() {

        Given("following \"known areas\":") { data: DataTable ->
            state["areas"] = data.asList()
        }

        Given("following \"weather condition\":") { data: DataTable ->
            state["weather"] = data.asList()
        }

    }

}