package clean.the.forest.weather.application

import clean.the.forest.weather.Area
import clean.the.forest.weather.AreaName
import clean.the.forest.weather.Country
import clean.the.forest.weather.GeoPos
import clean.the.forest.weather.model.WeatherCondition
import clean.the.forest.weather.model.WeatherReport
import java.time.LocalDateTime


class CheckWeatherUseCase {

    private val iñapaburu = Area(
        name = "Ipiñaburu",
        position = GeoPos(lat = 43.07, lon = -2.75),
        country = Country(code = "ES")
    )
    private val ibarra = Area(
        name = "Ibarra",
        position = GeoPos(lat = 43.05, lon = -2.57),
        country = Country(code = "ES")
    )
    private val zegama = Area(
        name = "Zegama",
        position = GeoPos(lat = 42.97, lon = -2.29),
        country = Country(code = "ES")
    )

    private val knownAreas: Map<AreaName, Area> =
        listOf(iñapaburu, ibarra, zegama)
            .associateBy { areaKey(it) }

    private val weatherCondition: Map<Area, WeatherCondition> =
        mapOf(
            iñapaburu to "Clouds",
            ibarra to "Clear",
            zegama to "Drizzle"
        )

    fun report(area: Area): WeatherReport {
        findByName(area.name)
            .let { weatherCondition ->
                return WeatherReport(
                    area = area,
                    weatherCondition = weatherCondition,
                    date = LocalDateTime.now()
                )
            }
    }

    private fun findByName(name: String): WeatherCondition {
        val area = (knownAreas[areaKey(name)]
            ?: throw IllegalArgumentException("There is no area called [$name]"))
        return weatherCondition[area]!!
    }

    private fun areaKey(area: Area): String = areaKey(area.name)
    private fun areaKey(name: String): String = name.toLowerCase()

}