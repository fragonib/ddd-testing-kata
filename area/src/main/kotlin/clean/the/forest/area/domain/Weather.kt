package clean.the.forest.area.domain

import java.time.LocalDateTime


data class WeatherReport(
        val area: Area,
        val weatherCondition: WeatherCondition,
        val date: LocalDateTime
) {
    constructor(area: Area, weatherCondition: WeatherCondition) :
            this(area, weatherCondition, LocalDateTime.now())

    val checkable: Boolean
        get() {
            return RelevantWeatherCondition.isClear(weatherCondition)
        }
}

typealias WeatherCondition = String

enum class RelevantWeatherCondition(val literal: String) {
    CLEAR("Clear");

    companion object {
        fun isClear(weather: WeatherCondition): Boolean {
            return CLEAR.literal == weather
        }
    }

}
