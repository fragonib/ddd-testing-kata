package clean.the.forest.weather.infraestructure

import clean.the.forest.weather.model.GeoPos
import clean.the.forest.weather.model.WeatherCondition

interface WeatherProvider {

    fun reportWeatherByGeoPos(geoPos: GeoPos): WeatherCondition

}