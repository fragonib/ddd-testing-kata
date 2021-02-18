package clean.the.forest.weather.infraestructure

import clean.the.forest.weather.model.Area
import reactor.core.publisher.Mono

interface AreaRepository {

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