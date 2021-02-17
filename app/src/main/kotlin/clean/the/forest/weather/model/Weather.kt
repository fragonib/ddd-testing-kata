package clean.the.forest.weather.model

import clean.the.forest.weather.Area
import java.time.LocalDateTime


typealias WeatherCondition = String

data class WeatherReport(
    val area: Area,
    val weatherCondition: WeatherCondition,
    val date: LocalDateTime
)