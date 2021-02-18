package clean.the.forest.weather.infrastructure

import clean.the.forest.weather.model.*
import reactor.core.publisher.Mono


class InMemoryAreaRepository : AreaRepository {

    private var knownAreas: Map<AreaName, Area> =
        listOf(
            Area(
                name = "Ipiñaburu",
                position = GeoPos(lat = 43.07, lon = -2.75),
                country = Country(code = "ES")
            ),
            Area(
                name = "Ibarra",
                position = GeoPos(lat = 43.05, lon = -2.57),
                country = Country(code = "ES")
            ),
            Area(
                name = "Zegama",
                position = GeoPos(lat = 42.97, lon = -2.29),
                country = Country(code = "ES")
            )
        ).associateBy { areaKey(it) }

    override fun findByName(name: String): Mono<Area> {
        return Mono.just(name)
            .map { areaKey(name) }
            .map { knownAreas[areaKey(name)] ?: throw IllegalArgumentException("There is no area called [$name]") }
    }

    private fun areaKey(area: Area): String = areaKey(area.name)

    private fun areaKey(name: String): String = name.toLowerCase()

}