package clean.the.forest.area.application

import clean.the.forest.area.domain.GeoPos
import clean.the.forest.area.domain.WeatherCondition
import reactor.core.publisher.Mono


interface WeatherProvider {

    fun byGeoPosition(geoPos: GeoPos): Mono<WeatherCondition>

}
