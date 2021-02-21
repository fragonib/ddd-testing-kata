package clean.the.forest.area.infrastructure

import clean.the.forest.area.model.*
import reactor.core.publisher.Flux
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

    private fun areaKey(name: String): String = name.toLowerCase()

}
