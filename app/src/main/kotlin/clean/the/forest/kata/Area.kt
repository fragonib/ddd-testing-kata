package clean.the.forest.kata

import java.time.LocalDateTime


typealias AreaName = String

data class Area(
    val name: AreaName,
    val position: GeoPos,
    val country: Country
)

data class GeoPos(
    val lat: Double,
    val lon: Double
)

data class Country(
    val code: String
)


typealias WeatherCondition = String

data class WeatherReport(
    val area: Area,
    val weatherCondition: WeatherCondition,
    val date: LocalDateTime
)