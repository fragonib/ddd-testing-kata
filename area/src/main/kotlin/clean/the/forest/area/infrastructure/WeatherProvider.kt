package clean.the.forest.area.infrastructure

import clean.the.forest.area.model.GeoPos
import clean.the.forest.area.model.WeatherCondition
import reactor.core.publisher.Mono

interface WeatherProvider {

    fun byGeoPosition(geoPos: GeoPos): Mono<WeatherCondition>

}