package clean.the.forest.area.functional

import clean.the.forest.shared.testing.ScenarioState
import clean.the.forest.area.model.Area
import io.cucumber.datatable.DataTable
import io.cucumber.java8.En


class BackgroundSteps(private val state: ScenarioState) : En {

    init {
        setupSteps()
    }

    private fun setupSteps() {

        Given("following \"known areas\":") { data: DataTable ->
            val knownAreas = data.cells()
                .drop(/* header size */ 1)
                .map { (areaName, lat, lon, country) ->
                    Area(areaName, lat.toDouble(), lon.toDouble(), country)
                }
                .associateBy { it.name }
            state["knownAreas"] = knownAreas
        }

        Given("following \"weather condition\":") { data: DataTable ->
            val weatherConditions = data.cells()
                .drop(/* header size */ 1)
                .map { (areaName, weatherCondition) -> Pair(areaName, weatherCondition) }
                .associate { it }
            state["weatherConditions"] = weatherConditions
        }

    }

}