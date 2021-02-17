package clean.the.forest.weather.infraestructure

import clean.the.forest.weather.model.Area

interface InMemoryAreaRepository {

    /**
     * Find a known area given its name
     *
     * @param name Area name
     *
     * @return Area data if known
     *
     * @throws IllegalArgumentException If there is no area known with given name
     */
    fun findByName(name: String): Area

}