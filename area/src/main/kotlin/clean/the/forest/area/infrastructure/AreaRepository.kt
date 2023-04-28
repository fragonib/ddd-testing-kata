package clean.the.forest.area.infrastructure

import clean.the.forest.area.model.Area
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AreaRepository {

    /**
     * Return all known areas so far
     *
     * @return A stream of areas
     */
    fun allKnown(): Flux<Area>

    /**
     * Find a known area given its name
     *
     * @param name Area name
     *
     * @return Area data if known
     *
     * @throws IllegalArgumentException If there is no area known with given name
     */
    fun findByName(name: String): Mono<Area>

    /**
     * Add an area to the known area collection
     *
     * @param area Area name
     *
     * @return Area data if known
     *
     * @throws ConflictWithExistingArea If there is any previous area with given name
     */
    fun addArea(area: Area): Mono<Area>

}

class ConflictWithExistingArea(message:String): RuntimeException(message)
