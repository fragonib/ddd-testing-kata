package clean.the.forest.area.model

import java.time.LocalDateTime


typealias WeatherCondition = String

data class WeatherReport(
    val area: Area,
    val weatherCondition: WeatherCondition,
    val date: LocalDateTime
) {
    val checkable: Boolean
        get() { return weatherCondition == "Clear" }
}