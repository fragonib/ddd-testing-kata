package clean.the.forest.weather.infrastructure

import clean.the.forest.weather.model.Area
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AreaRepository {

    /**
     * Return all known areas so far
     *
     * @return A stream of areas
     */
    fun findAll(): Flux<Area>

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
}