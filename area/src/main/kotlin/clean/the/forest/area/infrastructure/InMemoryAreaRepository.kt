package clean.the.forest.area.infrastructure

import clean.the.forest.area.model.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


class InMemoryAreaRepository : AreaRepository {

    private var knownAreas: Map<AreaName, Area> =
        listOf(
            Area(
                name = "Calderona",
                position = GeoPos(lat = 39.67, lon = -0.43),
                country = Country(code = "ES")
            ),
            Area(
                name = "Mariola",
                position = GeoPos(lat = 38.72, lon = -0.53),
                country = Country(code = "ES")
            ),
            Area(
                name = "Penyagolosa",
                position = GeoPos(lat = 40.23, lon = -0.29),
                country = Country(code = "ES")
            )
        ).associateBy { areaKey(it) }

    override fun allKnown(): Flux<Area> {
        return Flux.fromIterable(knownAreas.values)
    }

    override fun findByName(name: String): Mono<Area> {
        return Mono.just(name)
            .map { areaKey(it) }
            .map { knownAreas[it] ?: throw IllegalArgumentException("There is no area called [$name]") }
    }

    override fun addArea(area: Area): Mono<Area> {
        return Mono.fromCallable { areaKey(area) }
            .map { areaKey ->
                if (knownAreas.containsKey(areaKey))
                    throw ConflictWithExistingArea("There is yet known area called [${area.name}]")
                areaKey
            }
            .doOnNext { areaKey ->
                knownAreas = knownAreas + (areaKey to area)
            }
            .map { area }
    }

    private fun areaKey(area: Area): String = areaKey(area.name)

    private fun areaKey(name: String): String = name.lowercase()

}
