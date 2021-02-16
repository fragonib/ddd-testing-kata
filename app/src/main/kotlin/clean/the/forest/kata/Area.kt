package clean.the.forest.kata

import java.time.LocalDateTime


data class Area(
    val name: String,
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

data class WeatherReport(
    val area: Area,
    val weather: String,
    val date: LocalDateTime,
    val checkout: Boolean
)