package clean.the.forest.weather.infrastructure

import clean.the.forest.weather.model.GeoPos
import clean.the.forest.weather.model.WeatherCondition
import reactor.core.publisher.Mono

interface WeatherProvider {

    fun reportWeatherByGeoPos(geoPos: GeoPos): Mono<WeatherCondition>

}