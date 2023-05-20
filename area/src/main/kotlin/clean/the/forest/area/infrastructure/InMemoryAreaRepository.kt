package clean.the.forest.area.infrastructure

import clean.the.forest.area.application.AreaNotPresent
import clean.the.forest.area.application.AreaRepository
import clean.the.forest.area.application.ConflictWithExistingArea
import clean.the.forest.area.domain.*
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
                .filter { isPresent(it) }
                .switchIfEmpty( Mono.error(AreaNotPresent("There is no area called [$name]")) )
                .map(::retrieveArea)
    }

    override fun addArea(area: Area): Mono<Area> {
        return Mono.just(area)
                .filter { !isPresent(it) }
                .switchIfEmpty( Mono.error(ConflictWithExistingArea("There is yet known area called [${area.name}]")) )
                .doOnNext(::recordArea)
    }

    private fun isPresent(area: Area): Boolean = knownAreas.containsKey(areaKey(area))

    private fun isPresent(name: String): Boolean = knownAreas.containsKey(areaKey(name))

    private fun areaKey(area: Area): String = areaKey(area.name)

    private fun areaKey(name: String): String = name.lowercase()

    private fun recordArea(area: Area): Area {
        knownAreas = knownAreas + (areaKey(area) to area)
        return area
    }

    private fun retrieveArea(it: String): Area {
        return knownAreas[areaKey(it)]!!
    }

}
